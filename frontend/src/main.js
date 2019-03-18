import Vue from 'vue'
import './plugins/vuetify'
import App from './App.vue'
import router from './router'
import Vuetify from 'vuetify'
import 'vuetify/dist/vuetify.min.css'
import VueSession from 'vue-session'

Vue.config.productionTip = false
Vue.use(Vuetify)
Vue.use(VueSession)

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
