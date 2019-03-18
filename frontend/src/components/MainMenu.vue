<template>
    <v-toolbar dark>
        <v-toolbar-side-icon></v-toolbar-side-icon>
        <v-toolbar-title>Title</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-toolbar-items>
            <v-btn v-if="['/user'].includes($route.path)" @click="logout" class="right"
                   color="white" flat
            >Logout
            </v-btn>
        </v-toolbar-items>
    </v-toolbar>
</template>

<script>
    import {AXIOS} from '../components/http-common'
    export default {
        name: "mainmenu",
        methods: {
            logout() {
                let that = this;
                AXIOS.get('/logout')
                    .then(function (response) {
                        if (response.status === 200) {
                            that.$session.destroy();
                            that.$router.push('/');
                        }
                    })
            }
        }
    }
</script>

<style scoped>

</style>