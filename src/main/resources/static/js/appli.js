let app = Vue.createApp({
  
        data:()=>({
            user:{},
            cars:[],
            plannings:[],
        }),
        mounted: function(){
            this.loadUser()
        },
        template: `
        <Menu :user="user" :cars="cars" :plannings="plannings"/>`,
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
        <travel/>
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

    /**
     * @author Christian
     */
    app.component('planning',{
        props:{
            plannings:Array,

        },
        data:()=>({
            options:{
                year:"2-digit",
                month:"2-digit",
                day:"2-digit",
                hour:"2-digit",
                minute:"2-digit",
                second:"2-digit"
            },
        }),
        template:`
        <button @click="ajoutEvenement"> Ajouter un Evenement </button>
        <p>
            <ul>
                <li v-for="slot in plannings">
                    <p>Name : {{slot.slotName}}
                    De : {{new Date(slot.start).toLocaleDateString("en-US",options)}} 
                    jusqu'à : {{new Date(slot.end).toLocaleDateString("en-US",options)}}
                    </p>  
                </li>
            </ul>
        </p>
        
        `,
        methods:{

            ajoutEvenement :async function(){
                let newSlot={}
                let res = await fetch('/api/slots',{
                    method: 'PUT',
                    headers: {'Content-Type':'application/json'},
                    body: JSON.stringify(newslot)
                })
                let body = await res.json()
                this.plannings.push(body)
                return body;
            },

            loadData: async function(){
                console.log("load data")
                let res = await fetch('/api/slots') // hard coded :(, not HATEOAS
                console.log(res)

                let body = await res.json()
                console.log(body)

                this.slots = body._embedded.slots
            },
            request: async function(path){
                let res = await fetch(path) 
                let body = await res.json()
                return body
            },


        }
    })


    /**
     * @author Christian
     */

    
     app.component('addEvent',{
        
        data:()=>({
            name:null,
            daystart:null,
            monthstart:null,
            yearstart:null,
            heurestart:null,
            minutestart:null,
            dayend:null,
            monthend:null,
            yearend:null,
            heureend:null,
            minuteend:null,

        }),

        template:`
        <h1> Adding your Event </h1>
        <h2>From an Ics File</h2>
        <form role=" " @submit="/adding" method="POST">
            <label>Url link : </label>
            <input type="text" name="url" placeholder="name" required="required">
            <input type="submit">
        </form>

        <h2>Personal insertion</h2>
         <form role="form" action="/adding" method="POST">
            <label>
                Name of this event :
                <input type="text" {{slotOther.slotName}}" placeholder="name" required="required">
            </label>
            <br>
            <label>
                Start at :
                <input v-model="dayOfMonth" type="number" min="1" max="31"  "{{dateDayOfMonth}}">
                <input v-model="month" type="number" min="1" max="12" "{{dateMonth}}">
                <input v-model="year" type="number" th:value="{{dateYear}}">
                <input v-model="hour" type="number" min="0" max="23" "{{dateHour}}">
                <input v-model="minute" type="number" min="0" max="59"h "{{dateMinute]}">
            </label>
            <br>
            <label>
               End at :
               <input v-model="enddayOfMonth" type="number" min="1"max="31" {{enddateDayOfMonth}}">
               <input v-model="endmonth" type="number" min="1" max="12""{{enddateMonth}}">
               <input v-model="endyear" type="number" th:min="{{dateYear}}" "{{enddateYear}}">
               <input v-model="endhour" type="number" min="0" max="23" "{{enddateHour}}">
               <input v-model="endminute" type="number" min="0" max="59" {{enddateMinute}}">
            </label>
            <label>
               Is for me ?
               <input v-model="me" type="checkbox" checked="checked">
            </label>
            <input type="submit">
            </form>
        
        `,

        methods:{

            request: async function(path){
                let res = await fetch(path) 
                let body = await res.json()
                return body
            },

            adding: async function(){
                let newslot = {name:this.name, daystart:this.daystart, monthstart:this.monthstart,
                    yearstart:this.yearstart, heurestart:this.heurestart, minutestart:this.minutestart,
                dayend:this.dayend, monthend:this.monthend, yearend:this.yearend,heureend:this.heureend,
                minuteend:this.minuteend, owner:this.user} 
                console.log(newslot)
            
                let res = await fetch('/api/addEvent',{
                    method:'POST',
                    headers:{'content-type':'application/json'},
                    body : JSON.stringify(newslot)
                })
                let body= await res.json()
                this.slot.push(body)
            }
        },

    }) 


    app.component('travel',{
        template:`
            <div id="demoMap" style="height: 500px; width: 1000px"></div>

        `,
        mounted() {
            let travelManagement = document.createElement('script')
            travelManagement.setAttribute('src', '/js/travelManagement.js')
            document.head.appendChild(travelManagement)
          },
          methods:{
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
