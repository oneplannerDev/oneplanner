angular.module('adminApp').service('StatusService', function($rootScope, $state, $window, $http, $filter,RESOURCES, UtilsService) {
  var service = {
    SetCredentials: function (data) {
      //var authdata = Base64.encode(username + ':' + password);

      $rootScope.globals = {
          currentUser: {
              userId: data.userId,
              passwd: data.passwd,
              email: data.email,
              accessDate: data.accessDate
          }
      };
      // store user details in globals cookie that keeps user logged in for 1 week (or until they logout)
      var cookieExp = new Date();
      cookieExp.setDate(cookieExp.getDate() + 7);
      $window.localStorage.setItem('globals',JSON.stringify($rootScope.globals), { expires: cookieExp});
      //$cookies.putObject('globals',$rootScope.globals, { expires: cookieExp}, {path: '/' });//);
      console.log("$filter('json')(globals)====1"+$window.localStorage.getItem('globals'));
      console.log("$filter('json')(globals)====2"+$filter('json')(angular.fromJson($window.localStorage.getItem('globals')).currentUser));
  },

  GetCredentials: function () {
    //$rootScope.globals = $cookies.getObject('globals') || {};
    console.log("GetCredentials:"+$window.localStorage.getItem('globals'));
    if ($window.localStorage.getItem('globals') == null)  {
      $rootScope.globals = {};
    } else {
      $rootScope.globals = angular.fromJson($window.localStorage.getItem('globals'));
    }
    console.log("GetCredentials:"+$rootScope.globals);
    console.log("GetCredentials:"+$filter('json')($rootScope.globals));
    return $rootScope.globals;
  },

  ClearCredentials: function () {
    $window.localStorage.removeItem('globals');
    console.log("ClearCredentials:"+$window.localStorage.getItem('globals'));
    $rootScope.globals = {};
    //$cookies.remove('globals');
  },

  getStatus: function() {
      console.log("getStatus");
      //return $http.get('data/users.json', { cache: true }).then(function(resp) {
      //return $http.post('http://localhost:8081/oneplanner/subscr/userinfo/get'
      return $http.post(RESOURCES.SERVER_URL+'/admin/status/get',{ cache: true }).then(function(resp) {
        console.log("$filter('json')(resp.data)"+$filter('json')(resp.data));
        return resp.data.item;
      });
    },
    getUsers: function(params) {
      console.log("getUsers params="+$filter('json')(params));

      //return $http.get('data/users.json', { cache: true }).then(function(resp) {
      return $http.post(RESOURCES.SERVER_URL+'/admin/status/get/list',$filter('json')(params), { cache: true }).then(function(resp) {
      //console.log("$filter('json')(resp.data.item)"+$filter('json')(resp.data));
        if (resp.data.resultCode == "9999") {
          UtilsService.msgboxAlert("오류가 발생했습니다.");
        }
        $rootScope.introParams = params;
        return resp.data;
      });
    },
    loginAdmin: function(adminUser){
      //, okCallback, noUserCallback, noPasswdCallback, errorCallback) {
      console.log("loginAdmin");
      var SetCredentials = this.SetCredentials;
      //return $http.get('data/users.json', { cache: true }).then(function(resp) {
      //return $http.post('http://localhost:8081/oneplanner/subscr/userinfo/get'
      return $http.post(RESOURCES.SERVER_URL+'/admin/admin/login',$filter('json')(adminUser),{ cache: true }).then(function(resp) {
        console.log("$filter('json')(resp.data)"+$filter('json')(resp.data));
        // if (resp.data.resultCode == "4001") //user not found
        //   noUserCallback();
        // else if (resp.data.resultCode == "4005") //invalid passwd
        //   noPasswdCallback();
        // else if (resp.data.resultCode == "0001") //success
        //   okCallback(resp.data.item);
        // else
        //   errorCallback();
        if (resp.data.resultCode == "0001") { //success
           SetCredentials(resp.data.item);

         }

        return resp;
      });
    },
    updateAdmin: function(adminUser){
      //, okCallback, noUserCallback, noPasswdCallback, errorCallback) {
      console.log("updateAdmin");
      //return $http.get('data/users.json', { cache: true }).then(function(resp) {
      //return $http.post('http://localhost:8081/oneplanner/subscr/userinfo/get'
      return $http.post(RESOURCES.SERVER_URL+'/admin/admin/update',$filter('json')(adminUser),{ cache: true }).then(function(resp) {
        console.log("$filter('json')(resp.data)"+$filter('json')(resp.data));
        // if (resp.data.resultCode == "4001") //user not found
        //   noUserCallback();
        // else if (resp.data.resultCode == "4005") //invalid passwd
        //   noPasswdCallback();
        // else if (resp.data.resultCode == "0001") //success
        //   okCallback(resp.data.item);
        // else
        //   errorCallback();

          return resp;
      });
    },
  };
  return service;
})
