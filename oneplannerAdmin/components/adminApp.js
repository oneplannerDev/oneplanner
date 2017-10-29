'use strict';
var  app =  angular.module("adminApp",["ui.router", "ngCookies","ngAnimate","ui.bootstrap"]);

app.config(['$stateProvider','$urlRouterProvider',function($stateProvider, $urlRouterProvider ){
  var states = [
    // {name: 'default', url: '',
    //   templateUrl: "views/intro.html",
    //   controller: function() {
    //     console.log('intro controller');
    //   }
    // },
    //{name: 'parent', url: '/intro', abstract:true, component: 'intro'},
    {name: 'intro', url: '/intro/:from', component: 'introStat'},
    // {name: 'intro.basicStat', url: '/basicStat', component: 'introBasicStat'},
    // {name: 'intro.menuStat', url: '/menuStat', component: 'introMenuStat'},
    {name: 'account',url: '/acount',  component: 'adminUserAccount'},
    /* appUser 관리*/
    {name: 'appUserMgmt', url: '/appUsers', abstract:true,component: 'appUsers'},
    {name: 'appUsers', url: '/appUsers/:from', component: 'appUsers'},
    {name: 'appUser', url: '/appUser/view/:userId/:from', component: 'appUser'},
    /* Today 관리 */
    {name: 'todays', url: '/todays/:from', component: 'todays'},
    {name: 'today', url: '/admin/today/:today/:contSeq', component: 'today'},
    {name: 'todayAdd', url: '/admin/today/add',  component: 'todayAdd'},
    {name: 'todayUpdate', url: '/admin/today/update/:today/:contSeq', component: 'todayUpdate'},
    {name: 'todayDelete', url: '/admin/today/delete/:today/:contSeq', component: 'todayDelete'},

    {name: 'adminLogin', url: '/admin/login/',  component: 'adminLogin'},

    {name: 'adminUpdate', url: '/admin/update/',  component: 'adminUpdate'},
    // {name: 'admin.webusers.user', url: '/admin/webusers/{userId}',  component: 'webUser'},
    // {name: 'admin.webusers.user.add', url: '/admin/webusers/add',  component: 'webUserAdd'},
    // {name: 'admin.webusers.user.edit', url: '/admin/webusers/{userId}/edit',  component: 'webUserEdit'}
    //{name: '', url: '',  component: ''},
  ];
  states.forEach(function(state) {
    $stateProvider.state(state);
    console.log(state);
  });
     $urlRouterProvider
     .when("","intro({from:''})")
    .otherwise("intro({from:''})");
}]);

app.config(function($httpProvider) {
     $httpProvider.interceptors.push(function($q) {
        return {
          responseError: function(rejection) {
                if(rejection.status <= 0) {
                  bootbox.alert("서버의 반응이 없습니다.", function(){});
                  return;
                }
                return $q.reject(rejection);
            }
        };
    });
});
app.run(function($rootScope,$http,$state,$filter, $cookies,$window, $location,$uiRouter, StatusService){

  //window['ui-router-visualizer'].visualizer($uiRouter);
  $rootScope.$watch(function(){
    //console.log("$state.$current.name:"+$state.$current.name);
    return $state.$current.name;
  }, function(newVal, oldVal){
    console.log("newVal:"+newVal+" oldVal:"+oldVal+" state:"+$state.$current.name);
    $rootScope.previousState = oldVal;
    $rootScope.currentState = newVal;
    var globalforcheck = StatusService.GetCredentials();
    if (globalforcheck.currentUser == undefined) {
        $state.go('adminLogin');
        //$location.url('/admin/login/');
        console.log('adminLogin go'); // go to login
    } else {
      if ($state.$current.name == '') {
        console.log("ok "+$state.$current.name);
        $state.go('intro',{from:''});
      } else {
        console.log("not ok "+$state.$current.name);
      }

    }
  });


  $rootScope.$on('$stateChangeSuccess', function(ev, to, toParams, from, fromParams) {
      $rootScope.previousState = from.name;
      $rootScope.currentState = to.name;
      console.log('Previous state:'+$rootScope.previousState)
      console.log('Current state:'+$rootScope.currentState)
  });
});
