let app = Vue.createApp({
  
        data:()=>({
            user:Object,
            cars:Array,
            plannings:Array,
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
        template:`
        <button @click="ajoutEvenement"> Ajouter un Evenement </button>
        <p>
            <ul> <planning :pl="pl" v-for="pl in {{plannings}}"
                <p>Name : {{slot.name}}
                  De : {{slot.start}} 
                  jusqu'à : {{slot.end}}
                </p>  
            </ul>
        </p>
        
        `,
        methods:{

            ajoutEvenement :function(){

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
