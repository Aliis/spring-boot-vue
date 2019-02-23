<template>
    <v-dialog
            v-model="dialog"
            width="500"
    >
        <v-btn
            color="blue lighten-2"
            dark
            @click="dialog = true" flat slot="activator" >Login
        </v-btn>
        <v-card>

            <user/>

            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn
                        color="primary"
                        flat
                        @click="dialog = false"
                >
                    Close
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
    import user from './LoginModalContent'
    export default {
        name: 'signinmodal',
        components: {
            user
        },
        data () {
            return {
                dialog: false
            }
        }
    }
</script>