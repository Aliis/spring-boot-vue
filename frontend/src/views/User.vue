<template id="profile">
    <div>
        <mainMenu></mainMenu>
        <v-container>
            <div><h2>Hello, {{user}}</h2></div>
        </v-container>
    </div>
</template>
<script>
    import {AXIOS} from '../components/http-common'
    import mainMenu from '../components/MainMenu.vue'
    export default {
        name: 'User',
        components: {
            mainMenu: mainMenu
        },
        data() {
            return {
                user: '',
            }
        },
        beforeCreate: function () {
            if (!this.$session.exists()) {
                this.$router.push('/')
            }
        },
        mounted() {
            let that = this;
            AXIOS.get('/user/info')
                .then(function (response) {
                    that.user = response.data;
                })
        },
    }
</script>
<style>
</style>
