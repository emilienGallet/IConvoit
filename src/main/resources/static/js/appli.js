let app = Vue.createApp({
  
    template: `<Menu/>`,

    })


    app.component('Menu', {
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
        <profile/>
        </div>

        <div v-if="page == 'planning'">
        <planning/>
        </div>

        <div v-if="page == 'travel'">
        <travel/>
        </div>

        <div v-if="page == 'Car'">
        <car/>
        </div>

        <div v-if="page == 'findTravel'">
        <findTravel/>
        </div>
        `
    })

    app.component('profile',{
        template:`profile`,
    })

    app.component('planning',{
        template:`planning`,
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
    })

    app.component('Car',{
        template:`car`,
    })

   
	/**
 	 * @author Emilien Gallet
 	 */

	app.component('findTravel',{
	        template:`<p>No travel are avaiable</p><listTravel/>`,
			methods:{
				loadData: async function() {
				let res = await fetch('/api/vegetables'); // hard coded :(, not HATEOAS 
				let body = await res.json();
				this.veges = body._embedded.vegetables;
			},
		},
    })

	/* End */
    


    app.mount('#container')
