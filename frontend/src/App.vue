<template>
  <v-app>
    <v-content>
      <router-view></router-view>
    </v-content>
  </v-app>
</template>

<script>
    export default {
      name: 'App',
      data () {
        return {
          email: ''
        }
      }
    }
</script>
<style>
  html {   overflow-y: auto!important;}
  .v-dialog__activator {
    height: 100%;
  }
  h1, h2 {
    font-weight: normal;
    margin-bottom: 10px;
  }
</style>



