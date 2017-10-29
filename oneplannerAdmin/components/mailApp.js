'use strict';
var  app =  angular.module("mailApp",["ui.router","ngAnimate","ui.bootstrap"]);

app.config(['$stateProvider','$urlRouterProvider',function($stateProvider, $urlRouterProvider ){
  var states = [
    {name: 'askReset', url: '/askReset', component: 'askReset'},
    {name: 'resetPasswd', url: '/resetPasswd', component: 'resetPasswd'},
  ];
  states.forEach(function(state) {
    $stateProvider.state(state);
    console.log(state);
  });
     $urlRouterProvider
     //.when("","/intro")
    .otherwise("/intro");
}]);


app.run(function($rootScope,$http,$state,$filter, $location,$uiRouter, StatusService){
  window['ui-router-visualizer'].visualizer($uiRouter);
  $rootScope.loginInfo = {};
  //$rootScope.$state = $state;
  $rootScope.$watch(function(){
      return $state.$current.name;
  }, function(newVal, oldVal){
    var userInfo = {};
    userInfo.userId = "jinwon";
    console.log("app.run:"+$filter('json')(userInfo));
  });

});
