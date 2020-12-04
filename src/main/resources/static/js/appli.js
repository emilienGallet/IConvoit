var timer = 0;
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
        <findTravel :user="user" />
        </div>
        `
})

app.component('profile', {
	props: {
		user: Object,
	},
	data: () => ({

	}),
	template: `
           
        
        
            <p>Name : {{user.name}}</p>
            <p>Firstname : {{user.firstname}}</p>
            
            `
	,

	mounted: function() {
		//  this.loadData()
	},
	methods: {
		loadData: async function() {
			console.log("load data")
			let res = await fetch('/api/peoples') // hard coded :(, not HATEOAS
			console.log(res)

			let body = await res.json()
			console.log(body)

			this.peoples = body._embedded.peoples
		},
		request: async function(path) {
			let res = await fetch(path)
			let body = await res.json()
			return body
		},

	},

})

app.component('display-map',{
    props: {
        index: Number,
        ways:Array,

	},
    mounted() {
        let travelManagement = document.createElement('script')
        travelManagement.setAttribute('src', '/js/miniMap.js')
        document.head.appendChild(travelManagement)
        this.choosePath()
    },

    template:`
        <div id="miniMap" style="height: 250px; width: 500px"></div>


    `,
    methods:{
        choosePath: async function (){
            points = await this.request(this.ways[this.index]._links.points.href)
            miniList = []
            miniList = points._embedded.localizations
            showPath()
        },
        request: async function(path){
            let res = await fetch(path) 
            let body = await res.json()
            return body
        },
    }
    
})

app.component('planning', {
	props: {
		plannings: Array,

	},
	template: `planning
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
          startLon:"",
          startLat:"",
          endLon  :"",
          endLat  :"",
          year:new Date().getFullYear(),
          month:new Date().getMonth()+1,
          day:new Date().getDate(),
          hour:new Date().getHours(),
          minute:new Date().getMinutes(),
          selected:0,
          trajectName:"",


        }),

        template:`
        
            <div id="demoMap" style="height: 500px; width: 1000px"></div>
            <display-map :ways="ways" :index="1"/>
            <form @submit.prevent="addTravel">
                <input type="text" id="startLon" name="startLon" v-model="startLon" hidden>
                <input type="text" id="startLat" name="startLat" v-model="startLat" hidden>
                <input type="text" id="endLon"   name="endLon"   v-model="endLon"   hidden>
                <input type="text" id="endLat"   name="endLat"   v-model="endLat"   hidden>

                <input type="text" id="trajectName" name="trajectName" v-model="trajectName" placeholder="Traject Name" required >
                <select v-model="selected">
                    <option v-for="(car, index) in cars" :value ="index" >{{cars[index].registration}}</option>
                </select>
                <input type="submit">
            </form >
            <label>
                Start at :
                <input name="dayOfMonth" type="number" min="1" max="31" v-model="day" >
                <input name="month" type="number" min="1" max="12" v-model="month" >
                <input name="year" type="number" v-model="year">
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
                    console.log(points)
                    showPath()
            },
            addTravel: async function(){
                if(this.trajectName == "" || list1.length == 0)
                    return
                console.log("TRAJECT NAME",this.trajectName)
                console.log("DATE",new Date(
                this.year,
                this.month-1,
                this.day,
                this.hour,
                this.minute))
                console.log("selected",this.selected)
                console.log("list1",list1)
                console.log("localization",
                    startLon.value,
                    startLat.value,
                    endLon.value,
                    endLat.value, )

                console.log("START",list1[0])
                console.log("END",list1[list1.length-1])

                //path = {name:this.trajectName,points:list1}
                newPath.name= this.trajectName
                console.log("path",newPath)

                slotTravel = {
                slotName:this.trajectName,
                start:new Date(this.year,this.month-1,this.day,this.hour,this.minute),
                end:new Date(this.year,this.month-1,this.day,this.hour,this.minute),
              // participants:[this.user],
                startPlace:list1[0],
                finishPlace:list1[list1.length-1],
                paths:[newPath]
               }
               console.log("SLOT TRAVEL",slotTravel)


               let res = await fetch('/addtravel', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(slotTravel)
                })
            //    body = res.json()
             //   console.log(body)
            /*   let res = await fetch('/api/slots', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(slotTravel)
                })
                body = res.json()
                console.log(body)*/
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

	methods: {
		request: async function(path) {
			let res = await fetch(path)
			let body = await res.json()
			return body
		},
	}
})


/**
   * @author Emilien Gallet
   */

app.component('findTravel', {
	data: () => ({
		travels: [{ "id": 1, "slotName": "y", "start": "2020-11-28T21:52:42.989982", "end": "2020-11-28T22:07:42.990018", "url": null, "uid": null, "lastModified": null, "participants": [], "startPlace": null, "finishPlace": null, "paths": [] },
		{ "id": 2, "slotName": "s", "start": "2020-11-28T21:52:42.990234", "end": "2020-11-28T22:07:42.990241", "url": null, "uid": null, "lastModified": null, "participants": [], "startPlace": null, "finishPlace": null, "paths": [] }],
	}),
	props: {
		user: Object,
	},
	mounted: function() {
		console.log("mounted")
		this.loadTravels()
	},
	template: `
	<ul v-if="travels.length!=0">
		<findTravelDisplay v-for="(travel,index) in travels" :aTravel="travel" :indexTravel="index" :travels="travels"></findTravelDisplay>
	</ul>
	<div v-else><p>No travel are avaiable</p></div><listTravel/>`,
	methods: {
		request: async function(path) {
			let res = await fetch(path)
			let body = await res.json()
			return body
		},
		loadTravels: async function() {
			/*let res = await fetch('/loadFindTravels', {
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify(this.user)
			})
			let body = await res.json()*/

			body = await this.request('/loadFindTravels');
			this.travels = body;

		}

	},
})
app.component('findTravelDisplay', {
	data: () => ({
		participants: [],
	}),
	props: ["aTravel", "indexTravel", "travels"],
	beforeMount: function() {
		console.log("beforeMount")
		this.loadParticipant()
	},
	updated: function() {
		console.log("MISE A JOUR")
		this.loadParticipant();

	},
	template: `
		<li>
		<<{{aTravel.id}}>>
			<form method="POST" @submit.prevent="join">
			"{{aTravel.slotName}}" De
		                {{aTravel.start}}
		                jusqu'à {{aTravel.end}} 
		                <ul>
		                    <displayParticipant v-for="(people,index) in participants" :aPeople="people" :indexPeople="index"></displayParticipant>
		                </ul>
		                <input type="text" :value="aTravel.id" name="idSlot" hidden>
		
		                <input type="submit" value="Join">
		    </form>
		</li>
		`,
	methods: {
		request: async function(path) {
			let res = await fetch(path)
			let body = await res.json()
			return body
		},
		loadParticipant: async function() {
			let idTravel = this.aTravel.id;
			//console.log("JE SUIS LE NUMERO " + idTravel)
			let res = await fetch('/findParticipant', {
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify(idTravel)
			})
			//console.log("DONE")
			let body = await res.json()
			this.participants = body;
			//console.log(body)
			//body = await this.request('/findOwner');
			//this.travels = body;

		},
		join: async function() {
			/* We can do that but it's unsafe! 
			should i use knowlege of my couses or security++ ? ><""
			console.log("Rejoint le voyage n°" + this.aTravel.id)
			let res = await fetch('/api/slotTravels/'+this.aTravel.id+'/participants', {
				method: 'PUT',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify(this.participants)
			})
			let body = await res.json()
			this.participants = body;
			console.log(this.participants)
			//body = await this.request('/findOwner');
			//this.travels = body;
			I choose security way no trust of front-end and do transaction in restController
			*/
			let idTravel = this.aTravel.id;
			let res = await fetch('/joinTravel', {
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify(idTravel)
			})
			let body = await res.json()
			this.travels = this.travels.splice(this.indexTravel, 1);
		},


	},

})

app.component('displayParticipant', {
	props: ["aPeople", "indexPeople"],
	template: `
	<li v-if="indexPeople == 0">
		Name :  {{aPeople.name}} Firstname : {{aPeople.firstname}} DRIVER
	</li>
	<li v-if="indexPeople > 0">
		Name :  {{aPeople.name}} Firstname : {{aPeople.firstname}}
	</li>
	
		`,
	methods: {
		request: async function(path) {
			let res = await fetch(path)
			let body = await res.json()
			return body
		},
	},

})

/* End */



app.mount('#container')
