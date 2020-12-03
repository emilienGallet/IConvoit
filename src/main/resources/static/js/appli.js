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
                // console.log(users)
                for(i=0; i< users.length; i++){
                    user = users[i].username
                    // console.log("user",user, username)
                    if(user == username.username){

                        this.user = users[i]
                // console.log("UTILISATEUR",this.user)

                        car = await this.request(users[i]._links.myCars.href)
                        car = car._embedded.cars
                        // console.log(car)
                        this.cars = car

                        this.plannings = this.user.slotOthers
                        for(j =0; j < this.user.slotTravel.length; j++){

                            this.plannings.push(this.user.slotTravel[i])
                        }
                        

                        

                        return
                    }
                }

                // console.log("USER",this.user)
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
        <car  :cars="cars" :user="user"/>
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
                    // console.log("load data")
                    let res = await fetch('/api/peoples') // hard coded :(, not HATEOAS
                    // console.log(res)

                    let body = await res.json()
                    // console.log(body)

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
        template:`car {{cars}}
        
         
        <h3>list of my car(s)</h3>
        <ul id="displayListCars">
            
            <p v-if="cars.size=0">
                <li>0 car</li>
            </p>

            <p v-else>
            <li v-for="car in cars">
                <form method="POST" @submit="deleteCarIndexVue(id)">
                {{car.registration}} {{car.nbOfSeats}} {{car.brand}} {{car.color}} 
                <input v-model="id" type="text" hidden>
                <input  class="supp" type="submit" value="X">
                </form>
                
            </li>
            </p>
            
        </ul>
    
    <h3>add a car</h3>

    <form id="addcarVue" @submit.prevent="addcarVue" method="post">
    <!-- <form action=/addcarVue method="POST"> -->
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
                let newCar={color : this.color,brand : this.brand,registration : this.registration,Format : this.Format, nbOfSeats : parseInt(this.nbOfSeats), owner : this.user}
				console.log(newCar)                
			    let res = await fetch('/api/cars', {
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify(newCar)
			})
			let body = await res.json()
			this.cars.push(body)
                return body;
            },
            deleteCarIndexVue: async function(v){
                console.log("deleteCarIndexVue" + v)
                console.clear()
                console.log(this.cars[v]._links.self.href)
                let href = this.cars[v]._links.self.href;
                this.cars.splice(v,1)
                
                let res = await fetch(href, {
                    method: 'DELETE',
                    headers: { 'Content-Type': 'application/json' },
                })
                let body = await res.json()
                this.cars.push(body)
                
            }/*
            AddCar:async function(c,color,brand,registration,nbOfSeats){
                let userD = new UserDetails
                // let sc = new SecurityContextHolder
                // userD =(UserDetails) sc.getContext().getAuthentication().getPrincipal();
                // People p = peopleDetailsService.findByUsername(userD.getUsername());
                if(c.verifRegistration(c.getRegistration()) == false){
                    return "/car";
                }
                c.setNbOfSeats(nbSeats);
                c.setOwner(p);
                p.addCar(c);
                carRep.save(c);
                return "redirect:/car";
            },
            
            AddCar:function(Car c,@ModelAttribute("nbSeats") int nbSeats){
                    UserDetails userD = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    People p = peopleDetailsService.findByUsername(userD.getUsername());
            
                    if(c.verifRegistration(c.getRegistration()) == false){
                        return "/car";
                    }
                    c.setNbOfSeats(nbSeats);
                    c.setOwner(p);
                    p.addCar(c);
                    
                    carRep.save(c);
                }
            },*/
        },
        
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
