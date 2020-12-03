let app = Vue.createApp({
        data:()=>({
            user:{},
            cars:[],
            plannings:{},
        }),
        mounted: function(){
            this.loadUser()
        },
        template: `
        <Menu :user="user" :cars="cars" :plannings="plannings"/>`,
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
                        }
                        if(slots.hasOwnProperty('slotTravels')){
                            if(slots.slotTravels.length > 0){
                                slots.slotTravels.sort(function(a,b){
                                    x = new Date(b.start) - new Date(a.start)
                                    return new Date(a.start) - new Date(b.start);
                                });
                            }
                        }
                        this.plannings = slots
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
        }),
        mounted: function(){
            this.loadParticipants()
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
            </div>
        </div>
        `,
        methods:{
            loadParticipants:async function(){
                res = await this.request(this.slot._links.participants.href)
                res = res._embedded.peoples
                this.participants = res
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
