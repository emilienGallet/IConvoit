let app = Vue.createApp({
  
        data:()=>({
            user:{},
            cars:[],
            plannings:[],
            ways:[],
        }),
        mounted: function(){
            this.loadUser()
        },
        template: `
        <Menu :user="user" :cars="cars" :plannings="plannings" :ways="ways" />`,
        methods:{
            init: async function(){
              await  this.loadUser();
             //  await this.loadData();
            },

            loadUser:async function(){
                username = await this.request('/user')
                 
                users = await this.request('/api/peoples')
                users = users._embedded.peoples
                console.log(users)
                for(i=0; i< users.length; i++){
                    user = users[i].username
                    console.log("user",user, username)
                    if(user == username.username){

                        this.user = users[i]
                        console.log("UTILISATEUR",this.user)

                        car = await this.request(users[i]._links.myCars.href)
                        car = car._embedded.cars
                        console.log(car)
                        this.cars = car

                        this.plannings = this.user.slotOthers
                        for(j =0; j < this.user.slotTravel.length; j++){

                            this.plannings.push(this.user.slotTravel[i])
                        }
                        ways = await this.request(users[i]._links.ways.href)
                       // console.log(ways)
                        ways = ways._embedded.paths
                        this.ways = ways


                        return
                    }
                }

                console.log("USER",this.user)
            },

            request: async function(path){
                let res = await fetch(path) 
                let body = await res.json()
                return body
            },

         
        }

    })


    app.component('Menu', {
        props:{
            user:Object,
            cars:Array,
            plannings:Array,
            ways:Array,

        },
        data: () => ({
            page: 'RIEN',
        }),
        template: `
        <header>
        <a @click="page = 'profile'"    class="btn menu" >Profile</a>
        <a @click="page = 'planning'"   class="btn menu" >Planning</a>
        <a @click="page = 'travel'"     class="btn menu" >Travel</a>
        <a @click="page = 'Car'"        class="btn menu" >Car</a>
        <a @click="page = 'findTravel'" class="btn menu" >FindTravel</a>

        <a href="/logout" class="btn logout" >Logout</a>
        </header>
        

        <div v-if="page == 'profile'">
        <profile :user="user" />
        </div>

        <div v-if="page == 'planning'">
        <planning  :plannings="plannings"/>
        </div>

        <div v-if="page == 'travel'">
        <travel  :user="user" :cars="cars" :plannings="plannings" :ways="ways"  />
        </div>

        <div v-if="page == 'Car'">
        <car  :cars="cars" />
        </div>

        <div v-if="page == 'findTravel'">
        <findTravel :user="user"/>
        </div>
        `
    })

    app.component('profile',{
        props:{
            user:Object,
        },
        data: () => ({
           
            }),
        template:`
           
        
        
            <p>Name : {{user.name}}</p>
            <p>Firstname : {{user.firstname}}</p>
            
            `
            ,

            mounted: function (){
              //  this.loadData()
            },
            methods: {
                loadData: async function(){
                    console.log("load data")
                    let res = await fetch('/api/peoples') // hard coded :(, not HATEOAS
                    console.log(res)

                    let body = await res.json()
                    console.log(body)

                    this.peoples = body._embedded.peoples
                },
                request: async function(path){
                    let res = await fetch(path) 
                    let body = await res.json()
                    return body
                },
                
            },

    })

    app.component('planning',{
        props:{
            plannings:Array,

        },
        template:`planning
            {{plannings}}
        `,
        methods:{
            request: async function(path){
                let res = await fetch(path) 
                let body = await res.json()
                return body
            },
        }
    })

    app.component('travel',{
        props:{
            user:Object,
            cars:Array,
            plannings:Array,
            ways:Array,
        },
        data: () => ({
          test:"",
          startLon:"",
          startLat:"",
          endLon  :"",
          endLat  :"",
          year:new Date().getFullYear(),
          month:new Date().getMonth()+1,
          day:new Date().getDate(),
          hour:new Date().getHours(),
          minute:new Date().getMinutes(),
          selected:0


        }),

        template:`
        
            <div id="demoMap" style="height: 500px; width: 1000px"></div>
            
            <form @submit.prevent="addTravel">
                <input type="text" id="startLon" name="startLon" v-model="startLon" hidden>
                <input type="text" id="startLat" name="startLat" v-model="startLat" hidden>
                <input type="text" id="endLon"   name="endLon"   v-model="endLon"   hidden>
                <input type="text" id="endLat"   name="endLat"   v-model="endLat"   hidden>
                <input type="text" id="test"     name="test"     v-model="test" placeholder="Je suis visible ">

                <input type="text" id="trajectName" name="trajectName" placeholder="Traject Name">
                <select v-model="selected">
                    <option v-for="(car, index) in cars" :value ="index" >{{cars[index].registration}}</option>
                </select>
                <input type="submit">
            </form >
            <label>
                Start at :
                <input name="dayOfMonth" type="number" min="1" max="31" v-model="year" >
                <input name="month" type="number" min="1" max="12" v-model="month" >
                <input name="year" type="number" v-model="day">
                <input name="hour" type="number" min="0" max="23" v-model="hour">
                <input name="minute" type="number" min="0" max="59" v-model="minute">
            </label>   
            <ul>
                <li v-for="(way,index) in ways"><button @Click="choosePath(index)">{{way.name}}</button></li>
            </ul>

        `,
        mounted() {
            let travelManagement = document.createElement('script')
            travelManagement.setAttribute('src', '/js/travelManagement.js')
            document.head.appendChild(travelManagement)
          },
        methods:{
            createPath: async function(){
                let res = await fetch('/createPath', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({oldPass:oldPass,newPass:newPass})
                })
            },

            choosePath: async function (index){
                points = await this.request(this.ways[index]._links.points.href)
                list1 = []
                list1 = points._embedded.localizations
                    console.log(list1)
                    showPath()
            },
            addTravel:function(){
                console.log("addTravel",new Date(
               this.year,
               this.month-1,
               this.day,
               this.hour,
               this.minute))
               console.log("selected",this.selected)
            },

            request: async function(path){
                let res = await fetch(path) 
                let body = await res.json()
                return body
            },
        }
    })

    app.component('Car',{
        props:{
            cars:Array,
        },
        template:`car
        {{cars}}
        `,

        methods:{
            request: async function(path){
                let res = await fetch(path) 
                let body = await res.json()
                return body
            },
        }
    })

   
	/**
 	 * @author Emilien Gallet
 	 */

	app.component('findTravel',{
        props:{
            user:Object,

        },
	        template:`{{user}}<p>No travel are avaiable</p><listTravel/>`,
			methods:{
				loadData: async function() {
				let res = await fetch('/api/vegetables'); // hard coded :(, not HATEOAS 
				let body = await res.json();
				this.veges = body._embedded.vegetables;

                },
                request: async function(path){
                    let res = await fetch(path) 
                    let body = await res.json()
                    return body
                },
		},
    })

	/* End */
    


    app.mount('#container')
