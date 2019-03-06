<template>
  <v-app>
    <v-content>
      <v-toolbar dark>
        <v-toolbar-side-icon></v-toolbar-side-icon>
        <v-toolbar-title>Title</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-toolbar-items>
          <v-btn href="/api/logout" class="right" color="white" flat >Logout</v-btn>
        </v-toolbar-items>
      </v-toolbar>
      <router-view></router-view>
    </v-content>
  </v-app>
</template>

<script>
    export default {
      name: 'App',
      data () {
        return {
          //
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



