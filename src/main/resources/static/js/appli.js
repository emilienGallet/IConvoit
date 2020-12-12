var timer = 0;
let app = Vue.createApp({
        data:()=>({
            user:{},
            cars:[],
            ways:[],
            plannings:{},
        }),
        mounted: function(){
            this.loadUser()
        },
        template: `
        <Menu :user="user" :cars="cars" :plannings="plannings" :ways="ways" />`,
        methods:{
            init: async function(){
              await  this.loadUser();
            },
            loadUser:async function(){
                username = await this.request('/user')
                users = await this.request('/api/peoples')
                users = users._embedded.peoples
                for(i=0; i< users.length; i++){
                    user = users[i].username
                    if(user == username.username){
                        this.user = users[i]
                        car = await this.request(users[i]._links.myCars.href)
                        car = car._embedded.cars
                        this.cars = car

                        slots = await this.request(users[i]._links.reserved.href)
                        slots = slots._embedded
                        console.log(slots)
                        if(slots.hasOwnProperty('slotOthers')){
                            if(slots.slotOthers.length > 0){
                                slots.slotOthers.sort(function(a,b){
                                    x = new Date(b.start) - new Date(a.start)
                                    return new Date(a.start) - new Date(b.start);
                                });
                            }
                        }else{
                            slots.slotOthers = []
                        }

                        if(slots.hasOwnProperty('slotTravels')){
                            if(slots.slotTravels.length > 0){
                                slots.slotTravels.sort(function(a,b){
                                    x = new Date(b.start) - new Date(a.start)
                                    return new Date(a.start) - new Date(b.start);
                                });
                            }
                        }else{
                            slots.slotTravels = []
                        }

                        this.plannings = slots
                        console.log("this.plannings",this.plannings)
                        ways = await this.request(users[i]._links.ways.href)
                        // console.log(ways)
                         ways = ways._embedded.paths
                         this.ways = ways
                        return
                    }
                }
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
            ways:Array,
            plannings:Object,

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
        <car  :cars="cars" :user="user" />
        </div>

        <div v-if="page == 'findTravel'">
        <findTravel :user="user" :plannings="plannings"/>
        </div>
        `
})

app.component('profile',{
    props:{
        user:Object,
    },
    data: () => ({
       oldPass: "",
       newPass:"",
       fail:false,
       succes:false,
    }),
    template:`
        <p>Name : {{user.name}}</p>
        <p>Firstname : {{user.firstname}}</p>

        <form @submit.prevent="changePass" class="center"  >
            <h2 >Change password</h2>
            <h3 class="success" v-if="succes == true" >Password has been changed</h3> 
            <span class="error" v-if="fail == true" >Wrong Password</span> 
            <input class="form-horizontal" type="password" v-model="oldPass" placeholder="Password" required>
            <input class="form-horizontal" type="password" v-model="newPass" placeholder="New Password" required>
            <input class="form-horizontal" type="submit" value="Submit" >
        </form>

        `,
    methods: {
        changePass: async function(){
            if(this.oldPass == "" || this.newPass == ""){
                return
            }
            let res = await fetch('/changePass', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({oldPass:this.oldPass,newPass:this.newPass})
            })
            body = await res.json()

            this.oldPass= ""
            this.newPass=""
            this.succes = body.succes
            this.fail = !body.succes
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
            showPath(miniList)
        },
        request: async function(path){
            let res = await fetch(path) 
            let body = await res.json()
            return body
        },
    }
    
})



    app.component('display-slot-other',{
        props:{slot:Object},
        data:()=>({
            options:{
                year:"2-digit",
                month:"2-digit",
                day:"2-digit",
            
            },
            options2:{
                year:"2-digit",
                month:"2-digit",
                day:"2-digit",
               hour:"2-digit",
               minute:"2-digit",
               second:"2-digit"
            },
            visible:false,
        }),
    
        template:`
        {{new Date(slot.start).toLocaleDateString("en-US",options)}} 

        <div class="border">
            <span @click="visible = !visible">
            <h4>
            {{slot.slotName}}
            </h4>

            </span>
            <div class="offset" v-if="visible == true ">
                From {{new Date(slot.start).toLocaleDateString("en-US",options2)}} 
                <br>
                until : {{new Date(slot.end).toLocaleDateString("en-US",options2)}}
            </div>
        </div>
        `,
    })

    app.component('display-slot-travel',{
        props:{slot:Object},
        data:()=>({
            options:{
                year:"2-digit",
                month:"2-digit",
                day:"2-digit",
            
            },
            options2:{
                year:"2-digit",
                month:"2-digit",
                day:"2-digit",
               hour:"2-digit",
               minute:"2-digit",
               second:"2-digit"
            },
            visible:false,
            participants:[],
            ways:null,
            car:null,
        }),
        mounted: function(){
            this.loadParticipants()
            this.loadWays()
            this.loadCar()
            
        },
        template:`
        {{new Date(slot.start).toLocaleDateString("en-US",options)}} 

        <div class="border">
            <span @click="visible = !visible">
            <h4>
            {{slot.slotName}} 
            </h4>

            </span>
            <div class="offset" v-if="visible == true ">
                From {{new Date(slot.start).toLocaleDateString("en-US",options2)}} 
                <br>
                until : {{new Date(slot.end).toLocaleDateString("en-US",options2)}}
                <span v-for="(participant,index) in participants">
                    <br>
                    <span v-if="index == 0">Driver</span> 
                    {{participant.firstname}} {{participant.name}}
                </span>
                <br> Car :
                 {{car.color}} {{car.brand}} {{car.registration}}
                <display-map :ways="ways" :index="0"/>
            </div>
        </div>
        `,
        methods:{
            loadCar :async function(){
                res = await this.request(this.slot._links.car.href)
                this.car = res
            },
            loadParticipants: async function(){
                res = await this.request(this.slot._links.participants.href)
                res = res._embedded.peoples
                this.participants = res
            },
            loadWays : async function(){
                res = await this.request(this.slot._links.paths.href)
                this.ways = res._embedded.paths

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
            plannings:Object,
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
            visible:false,
        }),
        template:`
        <button @click="visible = !visible"> Add an event </button>
        <addEvent v-if="visible == true" :plannings="plannings"  @hide="visible = false"/>
        <p>
            <ul>
                <li v-for="slot in plannings.slotOthers">
                   <display-slot-other :slot="slot"/>
                </li>
            </ul>
        </p>
        <p>
        <ul>
            <li v-for="slot in plannings.slotTravels">
            <display-slot-travel :slot="slot"/>

            </li>
        </ul>
    </p>
        `,
        methods:{

        }
    })


    /**
     * @author Christian
     */
     app.component('addEvent',{
        props:{
            plannings:Object,
        },
        emits:['hide'],
        data:()=>({
            name:"",
            daystart:    new Date().getDate(),
            monthstart:  new Date().getMonth()+1,
            yearstart:   new Date().getFullYear(),
            hourestart:  new Date().getHours(),
            minutestart: new Date().getMinutes(),
            dayend:   new Date().getDate(),
            monthend: new Date().getMonth()+1,
            yearend:  new Date().getFullYear(),
            houreend: new Date().getHours(),
            minuteend:new Date().getMinutes(),
            me:null,
            url:null,

        }),

        template:`
        <div class="addEvent">
            <h1> Adding your Event </h1>
            <h2>From an Ics File</h2>
            <form @submit.prevent="addWithIcsFile" >
                <label>Url link : </label>
                <input type="text" name="url" placeholder="name" v-model="url"  required >
                <input type="submit">
            </form>

            <h2>Personal insertion</h2>
            <form  @submit.prevent="adding" >
            <label>
                Name of this event :
                <input type="text" v-model="name" placeholder="name" required>
            </label>
            <br>
            <label>
                Start at :
                <input v-model="daystart"    type="number" min="1" max="31" required>
                <input v-model="monthstart"  type="number" min="1" max="12" required>
                <input v-model="yearstart"   type="number"  required>
                <input v-model="hourestart"  type="number" min="0" max="23" required>
                <input v-model="minutestart" type="number" min="0" max="59" required>
            </label>
            <br>
            <label>
               End at :
               <input v-model="dayend" type="number" min="1" max="31" required>
               <input v-model="monthend"      type="number" min="1" max="12" required>
               <input v-model="yearend"       type="number"  required>
               <input v-model="houreend"       type="number" min="0" max="23" required>
               <input v-model="minuteend"     type="number" min="0" max="59" required>
            </label>
            <label>
               Is for me ?
               <input v-model="me" type="checkbox" checked="checked">
            </label>
            <input type="submit">
            </form>
        </div>
        
        `,
        methods:{
            addWithIcsFile : async function(){
                if(this.url == null){
                    return
                }
                let res = await fetch('addslotIcs',{
                    method:'POST',
                    headers:{'content-type':'application/json'},
                    body : JSON.stringify(this.url)
                })
                body = await res
                this.url = null
                this.$emit('hide')
            },

            adding: async function(){
                if(this.name == ""){
                    return
                }
                start = new Date(this.yearstart, this.monthstart-1,
                    this.daystart, this.hourestart, this.minutestart)

                end = new Date(this.yearend, this.monthend-1,
                    this.dayend, this.houreend, this.minuteend)

                if((end -start) < 0){
                    return
                }
                let newslot = {slotName:this.name, start,end} 
                this.plannings.slotOthers.push(newslot)
                
                let res = await fetch('addslot',{
                    method:'POST',
                    headers:{'content-type':'application/json'},
                    body : JSON.stringify(newslot)
                })
                body = await res
                this.$emit('hide')
                this.plannings.slotOthers.sort(function(a,b){
                    x = new Date(b.start) - new Date(a.start)
                    return new Date(a.start) - new Date(b.start);
                });
            }
        },
    }) 


    app.component('travel',{
        props:{
            user:Object,
            cars:Array,
            plannings:Object,
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
            choosePath: async function (index){
                points = await this.request(this.ways[index]._links.points.href)
                list1 = []
                list1 = points._embedded.localizations
                    console.log(list1)
                    console.log(points)
                    showPath()
            },

            addTravel: async function(){
                console.log(this.user._links.ways.href)
                
                if(this.trajectName == "" || list1.length == 0 || this.cars.length == 0)
                    return
                console.log("TRAJECT NAME",this.trajectName)
                console.log("DATE",new Date(this.year, this.month-1, this.day,
                            this.hour,this.minute))
                console.log("selected",this.selected)
                console.log("list1",list1)
                console.log("localization",
                    startLon.value,
                    startLat.value,
                    endLon.value,
                    endLat.value, )

                console.log("START",list1[0])
                console.log("END",list1[list1.length-1])

                console.log("path",newPath)
                
                newPaths = {name : this.trajectName, points : []}

                // Localization
                for(loc of newPath.points){
                    let res = await fetch('/api/localizations', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify(loc)
                    })
                    body = await res.json()
                    newPaths.points.push(body._links.self.href)
                }

                // Paths
                let res = await fetch('/api/paths', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(newPaths)
                })
                paths = await res.json()

                console.log("------",paths)
                console.log( "startPlace" ,newPaths.points[0],
                    "finishPlace" , newPaths.points[newPath.points.length-1])

                    //User Ways
                    console.log(this.user)

                    console.log(this.user._links.ways.href)

                    idPeople = this.user._links.self.href
                    idPath = paths._links.self.href
                    idPeople = idPeople.split("/")
                    idPeople = idPeople[idPeople.length-1]
                    idPath = idPath.split("/")
                    idPath = idPath[idPath.length-1]

                    res = await fetch('/api/peoples/search/linkPathPeople?idPeople='+idPeople+'&idPath='+idPath, {
                        method: 'GET',
                        headers: { 'Content-Type': 'application/json' },
                        })
               // rep = await res
               // console.log("REP  ",rep)
                // SlotTravel
                        res = await this.request(this.cars[this.selected]._links.self.href)
                        carT = await res
                        console.log("CAR----",car)

                slotTravel = {
                    slotName:this.trajectName,
                    start:new Date(this.year,this.month-1,this.day,this.hour,this.minute),
                    end:new Date(this.year,this.month-1,this.day,this.hour,this.minute),
                    participants:[this.user._links.self.href],
                    startPlace : newPaths.points[0],
                    finishPlace : newPaths.points[newPath.points.length-1],
                    paths : [paths._links.self.href],
                    car : this.cars[this.selected]._links.self.href,
                    limitParticipate : carT.nbOfSeats
                }

                console.log("CAR",this.cars[this.selected]._links.self.href)
                console.log("SLOT TRAVEL",slotTravel)


                let res2 = await fetch('/api/slotTravels', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(slotTravel)
                })
                sl = await res2.json()
                this.plannings.slotTravels.push(sl)
                this.ways.push(paths)
                console.log("SLOT TRAVEL",sl)
            
               this.startLon=""
               this.startLat=""
               this.endLon  =""
               this.endLat  =""
               this.year=new Date().getFullYear()
               this.month=new Date().getMonth()+1
               this.day=new Date().getDate()
               this.hour=new Date().getHours()
               this.minute=new Date().getMinutes()
               this.trajectName=""

               list1 = []
                showPath()
            },

         
            request: async function(path){
                let res = await fetch(path) 
                let body = await res.json()
                return body
            },
        }
    })

  /**
 	 * @author Mélanie EYRAUD
 	 */
     app.component('Car',{
        props:{
            cars : Array,
			user : Object,
        },
        data: () => ({
           registration:null,
           nbOfSeats:null,
           brand:"",
           Format:null,
           color:"",
           id:null
        }),
        template:`
        
         
        <h3>list of my car(s)</h3>
        <ul id="displayListCars">
            
            <p v-if="cars.size=0">
                <li>0 car</li>
            </p>
            <p v-else>
            <li v-for="(car,id) in cars">
                <form method="POST" @submit.prevent="deleteCarIndexVue(id)">
                {{car.registration}} {{car.nbOfSeats}} {{car.brand}} {{car.color}} 
                
                <input @onClick="deleteCarIndexVue(id)" class="supp" type="submit" value="X">
                </form>
                
            </li>
            </p>
            
        </ul>
    
    <h3>add a car</h3>
    <form id="addcarVue" @submit.prevent="addcarVue" method="post">
        <p><input v-model="color" type="text" placeholder="color" id="color" required/></p>
        <p><input v-model="brand" type="text" placeholder="brand" id="brand" required/></p>
        <p> model : LL-NNN-LL <input type="text" v-model="registration" placeholder="registration" id="registration" required/></p>
        <p>type of car (citadine = 5 seats maximum)
            <input v-model="Format" list="formats" id="Format" required>
            <datalist id="formats">
                <option value="citadine"></option>
                <option value="other"></option>
            </datalist>
        </p>
        <p>nb of seats
            <input v-model="nbOfSeats" type="number" min="1" max="5" required>
        </p>
        <input type="submit" value="Send">
    </form>
    
        `,

        methods:{
            request: async function(path){
                let res = await fetch(path) 
                let body = await res.json()
                return body
            },
            addcarVue: async function(){
                let newCar={color : this.color, brand : this.brand, registration : this.registration, format : this.Format,
                             nbOfSeats : parseInt(this.nbOfSeats), owner : this.user._links.self.href }
                
			    let res = await fetch('/api/cars', {
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify(newCar)
			    })
			    let body = await res.json()
				console.log("body",body)                
                this.cars.push(body)
                this.registration=null
                this.nbOfSeats=null
                this.brand=""
                this.Format=null
                this.color=""
                this.id=null
            
            },
            deleteCarIndexVue: async function(v){
                console.clear()
                console.log("deleteCarIndexVue" + v)
                console.log(this.cars[v]._links.self.href)
                let href = this.cars[v]._links.self.href;
                
                let res = await fetch(href, {
                    method: 'DELETE',
                    headers: { 'Content-Type': 'application/json' },
                })
                let body = await res
                if(body.ok){
                    this.cars.splice(v,1)
                }
            }
        },
        
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
        plannings:Object,
	},
	mounted: function() {
        console.log("mounted")
        console.log("this.plannings.slotTravels",this.plannings.slotTravels)
		this.loadTravels()
	},
	template: `
	<ul v-if="travels.length!=0">
		<findTravelDisplay v-for="(travel,index) in travels" :aTravel="travel" :indexTravel="index" :travels="travels"   :plannings="plannings" ></findTravelDisplay>
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
        ways:[]
	}),
	props: {aTravel:Object, indexTravel:Number, travels:Object,plannings:Object},
	beforeMount: function() {
		console.log("beforeMount")
		this.loadParticipant()
    },
    mounted: function() {
        console.log("findTravelDisplay")
        console.log("this.plannings.slotTravels",this.plannings.slotTravels)
	},
	updated: function() {
	//	console.log("MISE A JOUR")
      //  this.loadParticipant();

	},
	template: `
		<li>
		
			<form method="POST" @submit.prevent="join">
			"{{aTravel.slotName}}" De
		                {{aTravel.start}}
		                jusqu'à {{aTravel.end}} 
		                <ul>
		                    <displayParticipant v-for="(people,index) in participants" :aPeople="people" :indexPeople="index"></displayParticipant>
		                </ul>
		                <input type="text" :value="aTravel.id" name="idSlot" hidden>
                     <!--   <display-map :ways="ways" :index="1"/>
		-->
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
            
            res =  await this.request("/api/slotTravels/"+idTravel)
            slt = await res
            console.log(slt)

         //   console.log("this.travels[this.indexTravel]",this.travels[this.indexTravel])
            this.plannings.slotTravels.push(slt); 
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
