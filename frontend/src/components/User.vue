<template>
    <v-container>
        <h1>Create User</h1>

        <v-flex xs12>
            <v-text-field
                    label="First name"
                    v-model="user.firstName"
            ></v-text-field>
        </v-flex>
        <v-flex xs12>
            <v-text-field
                    label="Last name"
                    v-model="user.lastName"
            ></v-text-field>
        </v-flex>
        <v-flex
                xs12
                mb-5
        >
        <v-btn color="info" @click="createUser()" class="right">Create User</v-btn>
        </v-flex>
            <v-flex
                    xs12
                    mb-5
            >
        <div v-if="showResponse"><span>User created with Id: {{ response }}</span></div>

        <v-btn color="success" v-if="showResponse" @click="retrieveUser()">Retrieve user {{user.id}} data from database</v-btn>

        <h4 v-if="showRetrievedUser">Retrieved user {{retrievedUser.firstName}} {{retrievedUser.lastName}}</h4>
            </v-flex>
    </v-container>
</template>

<script>
import {AXIOS} from './http-common'

export default {
    name: 'user',
    data () {
        return {
            response: [],
            errors: [],
            user: {
                lastName: '',
                firstName: '',
                id: 0
            },
            showResponse: false,
            retrievedUser: {},
            showRetrievedUser: false
        }
    },
    methods: {
        // Fetches posts when the component is created.
        createUser () {
            var params = new URLSearchParams()
            params.append('firstName', this.user.firstName)
            params.append('lastName', this.user.lastName)

            AXIOS.post(`/user`, params)
                .then(response => {
                // JSON responses are automatically parsed.
                this.response = response.data
            this.user.id = response.data
            console.log(response.data)
            this.showResponse = true
        })
        .catch(e => {
                this.errors.push(e)
        })
        },
        retrieveUser () {
            AXIOS.get(`/user/` + this.user.id)
                .then(response => {
                // JSON responses are automatically parsed.
                this.retrievedUser = response.data
            console.log(response.data)
            this.showRetrievedUser = true
        })
        .catch(e => {
                this.errors.push(e)
        })
        }
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
    h1, h2 {
        font-weight: normal;
        margin-bottom: 10px;
    }
    .info {
        margin: 10px 0 0 0;
    }
    .success {
        margin: 10px 0;
    }
</style>