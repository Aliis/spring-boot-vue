<template>
    <v-container>
        <div class="login">
            <h3>Sign In / Create new user</h3>
            <div class="email-login">
                <v-alert class="alert"
                         :value="true"
                        v-model="alert"
                        dismissible
                        type="error"
                        outline
                >
                    {{loginAlertMessage}}
                </v-alert>
                <v-form>
                    <v-text-field v-model="email" label="Email" type="text"></v-text-field>
                    <v-text-field v-model="password" label="Password" type="text"></v-text-field>
                    <v-btn @click="submitEmailLogin" class="right" color="info" >Login</v-btn>
                </v-form>
            </div>
            <div class="google-login">
                <button @click="googleLogin" class="social-button">
                    <img alt="Google logo"
                         src="https://diylogodesigns.com/wp-content/uploads/2016/04/google-logo-icon-PNG-Transparent-Background-768x768.png">
                </button>
            </div>
            <div class="fb-login">
                <button @click="fbLogin" class="social-button">
                    <img alt="Facebook logo" src="http://www.stickpng.com/assets/images/584ac2d03ac3a570f94a666d.png">
                </button>
            </div>
        </div>
    </v-container>
</template>

<script>
    import firebase from 'firebase'
    import {AXIOS} from './http-common'

    const config = {
        apiKey: "AIzaSyB5am9vDj6XghD_atAZN0FKbW-9sZ0azMs",
        authDomain: "spring-boot-vue-1545055785370.firebaseapp.com",
        databaseURL: "https://spring-boot-vue-1545055785370.firebaseio.com",
        projectId: "spring-boot-vue-1545055785370",
        storageBucket: "spring-boot-vue-1545055785370.appspot.com",
        messagingSenderId: "217594607633"
    }

    firebase.initializeApp(config)

    export default {
        name: 'Login',
        data() {
            return {
                email: '',
                password: '',
                params: '',
                signInMethod: '',
                alert: false,
                loginAlertMessage: 'Something went wrong, let\'s take a coffee and try again'
            }
        },
        methods: {
            sendToDB(url) {
                let that = this;
                AXIOS.post(url, that.params).then(() => {
                    this.$session.start();
                    this.$router.push('/user')
                }).catch((err) => {
                  that.alert = true;
                });
            },
            submitEmailLogin() {
                let that = this;
                that.params = {
                    'userName': this.email,
                    'password': this.password
                }
                that.sendToDB('/login')
            },
            signInWithPopup(provider) {
                let that = this;
                firebase.auth().signInWithPopup(provider).then((result) => {
                    result.user.getIdToken().then(function(idToken) {
                        that.params = {
                            'uid': idToken,
                            // 'method': that.signInMethod
                        }
                        that.sendToDB('/login/firebase')
                    });
                }).catch(() => {
                    that.alert = true;
                });
            },
            googleLogin() {
                this.signInMethod = 'gl'
                this.signInWithPopup(new firebase.auth.GoogleAuthProvider());
            },
            fbLogin() {
                this.signInMethod = 'fb'
                this.signInWithPopup(new firebase.auth.FacebookAuthProvider())
            }
        },
    }
</script>

<style scoped>  /* "scoped" attribute limit the CSS to this component only */
p {
    margin-top: 40px;
    font-size: 13px;
}
.right {
    margin-right: 0;
}
.google-login {
    margin:  20px 0;
    clear: both;
}
.social-button {
}
.social-button img {
    width: 40px;
}

.alert{
    margin-top: 20px;
    margin-bottom: 40px;
}
</style>