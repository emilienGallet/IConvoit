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
                request: async function(path){
                    let res = await fetch(path) 
                    let body = await res.json()
                    return body
                },
		},
    })

	/* End */
    


    app.mount('#container')
