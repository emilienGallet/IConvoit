var timer = 0;
let app = Vue.createApp({

	data: () => ({
		user: {},
		cars: [],
		plannings: [],
		travels: [],
	}),
	mounted: function() {
		this.loadUser()
	},
	template: `
        <Menu :user="user" :cars="cars" :plannings="plannings"/>`,
	methods: {
		init: async function() {
			await this.loadUser();
			//  await this.loadData();
		},

		loadUser: async function() {
			username = await this.request('/user')

			users = await this.request('/api/peoples')
			users = users._embedded.peoples
			console.log(users)
			for (i = 0; i < users.length; i++) {
				user = users[i].username
				console.log("user", user, username)
				if (user == username.username) {

					this.user = users[i]
					console.log("UTILISATEUR", this.user)

					car = await this.request(users[i]._links.myCars.href)
					car = car._embedded.cars
					console.log(car)
					this.cars = car

					this.plannings = this.user.slotOthers
					for (j = 0; j < this.user.slotTravel.length; j++) {

						this.plannings.push(this.user.slotTravel[i])
					}




					return
				}
			}

			console.log("USER", this.user)
		},

		request: async function(path) {
			let res = await fetch(path)
			let body = await res.json()
			return body
		},


	}

})


app.component('Menu', {
	props: {
		user: Object,
		cars: Array,
		plannings: Array,

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

app.component('planning', {
	props: {
		plannings: Array,

	},
	template: `planning
            {{plannings}}
        `,
	methods: {
		request: async function(path) {
			let res = await fetch(path)
			let body = await res.json()
			return body
		},
	}
})

app.component('travel', {
	template: `
            <div id="demoMap" style="height: 500px; width: 1000px"></div>

        `,
	mounted() {
		let travelManagement = document.createElement('script')
		travelManagement.setAttribute('src', '/js/travelManagement.js')
		document.head.appendChild(travelManagement)
	},
	methods: {
		request: async function(path) {
			let res = await fetch(path)
			let body = await res.json()
			return body
		},
	}
})

app.component('Car', {
	props: {
		cars: Array,
	},
	template: `car
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
		<findTravelDisplay v-for="(travel,index) in travels" :aTravel="travel" :indexTravel="index"></findTravelDisplay>
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
	props: ["aTravel", "indexTravel"],
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
		{{aTravel}}
			<form method="POST" action="/findTravel" >
			"{{aTravel.slotName}}" De
		                {{aTravel.start}}
		                jusqu'à {{aTravel.end}} 
		                <ul>
		                    <displayParticipant v-for="people in participants" :aPeople="people"></displayParticipant>
		                </ul>
		                <input type="text" :value="aTravel.id" name="idSlot" hidden>
		
		                <input type="submit" value="Join" >
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
			console.log("JE SUIS LE NUMERO " + this.aTravel.id)
			let res = await fetch('/findOwner', {
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify(this.aTravel.id)
			})
			let body = await res.json()
			this.participants = body;
			console.log(this.participants)
			//body = await this.request('/findOwner');
			//this.travels = body;

		}

	},

})

app.component('displayParticipant', {
	props: ["aPeople"],
	template: `
	<li>
		Name :  {{aPeople[2]}} Firstname : {{aPeople[2]}}
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
