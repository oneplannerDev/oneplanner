angular.module('adminApp').service('UsersService', function($rootScope, $http, $filter,RESOURCES, UtilsService) {
  var service = {
    getAllUsers: function(params) {
      console.log("getAllUsers params="+$filter('json')(params));
      //return $http.get('data/users.json', { cache: true }).then(function(resp) {
      return $http.post(RESOURCES.SERVER_URL+'/admin/user/list',$filter('json')(params), { cache: true }).then(function(resp) {
      //console.log("$filter('json')(resp.data.item)"+$filter('json')(resp.data));
        $rootScope.usersParams = params;
        return resp.data;
      });
    },

    // getUser: function(id) {
    //   console.log("getUser:"+id);
    //   function userMatchesParam(user) {
    //     return user.userId === id;
    //   }
    //   return service.getAllUsers().then(function (users) {
    //     return users.find(userMatchesParam);
    //   });
    // }
    getUser: function(id) {
      console.log("getUser:"+id);
      //return $http.get('data/users.json', { cache: true }).then(function(resp) {
      //return $http.post('http://localhost:8081/oneplanner/subscr/userinfo/get'
      return $http.post(RESOURCES.SERVER_URL+'/admin/userinfo/get',{"userId":id}, { cache: true }).then(function(resp) {
        console.log("$filter('json')(resp.data)"+$filter('json')(resp.data));
        return resp.data.item;
      });
    },

    registerUser: function(user) {
      // myuser = {
      //   "userId": "mynameis",
      //   "userName": "mynameis",
      //   "email": "mynameis@gmail.com",
      //   "passwd": "MyName1@",
      //   "passwdConfirm": "MyName1@",
      //   "userType": "S",
      //   "authYn": "Y",
      //   "deleteYn": "N",
      //   "role": "A"
      // };
      var upData = {}
      upData.userId = myuser.userId;
      upData.userName = myuser.userName;
      upData.email = myuser.email;
      upData.passwd = myuser.passwd;
      upData.authYn = myuser.authYn;
      upData.deleteYn = myuser.deleteYn;
      upData.role= myuser.role;
      var data = JSON.stringify(upData);
      console.log("registerUser:"+data);
      //return $http.post('http://localhost:8081/oneplanner/admin/user/register'
      return $http.post(RESOURCES.SERVER_URL+'/admin/register/admin', data/*angular.toJson()*/, { cache: true }).then(function(resp) {
        result = false;
        console.log("$filter('json')(resp.data)"+$filter('json')(resp.data));
        if (resp.data.resultCode == "0001" || resp.data.resultCode == "0002") {
          result = true;
        } else {
          result = false;
        }
        return result;
      });
    },
    updateUser: function(user) {
      // myuser = {
      //   "userId": "mynameis",
      //   "userName": "mynameis",
      //   "email": "mynameis@gmail.com",
      //   "passwd": "MyName1@",
      //   "passwdConfirm": "MyName1@",
      //   "userType": "S",
      //   "authYn": "Y",
      //   "deleteYn": "N",
      //   "role": "A"
      // };
      var upData = {}
      upData.userId = myuser.userId;
      upData.userName = myuser.userName;
      upData.email = myuser.email;
      upData.passwd = myuser.passwd;
      upData.authYn = myuser.authYn;
      upData.deleteYn = myuser.deleteYn;
      upData.role= myuser.role;
      var data = JSON.stringify(upData);
      console.log("registerUser:"+data);
      //return $http.post('http://localhost:8081/oneplanner/subscr/userinfo/update'
      return $http.post(RESOURCES.SERVER_URL+'/admin/register/admin', data/*angular.toJson()*/, { cache: true }).then(function(resp) {
        result = false;
        console.log("$filter('json')(resp.data)"+$filter('json')(resp.data));
        if (resp.data.resultCode == "0001" || resp.data.resultCode == "0002") {
          result = true;
        } else {
          result = false;
        }
        return result;
      });
    },
    sendResetEmail: function(userId, langCode) {
      var data = {};
      data.userId = userId;
      data.langCode = langCode;

      console.log("sendResetEmail:"+data);
      var dlg = UtilsService.msgboxWait();

      //return $http.post('http://localhost:8081/oneplanner/subscr/userinfo/update'
      return $http.post(RESOURCES.SERVER_URL+'/subscr/request/passwdreset', data/*angular.toJson()*/, { cache: true }).then(function(resp) {
        console.log("$filter('json')(resp.data)"+$filter('json')(resp));
        console.log("sendResetEmail");
        if (resp.data.resultCode == "0001") {
          UtilsService.msgboxAlert("메일이 전송되었습니다.");
        } else if (resp.data.resultCode == "5001") {
          UtilsService.msgboxAlert("메일전송이 실패했습니다.");
        } else {
          UtilsService.msgboxAlert("오류가 발생했습니다.");
        }
        UtilsService.msgboxWaitClose(dlg);

        return resp.data.resultCode;
      },
      function(resp){
        console.log("sendResetEmail fail: "+$filter('json')(resp));
        UtilsService.msgboxAlert("오류가 발생했습니다.");
        return "9999";
      });
    },
    sendConfirmEmail: function(userId, langCode) {
      var data = {};
      data.userId = userId;
      data.langCode = langCode;

      console.log("sendResetEmail:"+data);
      var dlg = UtilsService.msgboxWait();
      //return $http.post('http://localhost:8081/oneplanner/subscr/userinfo/update'
      return $http.post(RESOURCES.SERVER_URL+'/subscr/request/authconfirm', data/*angular.toJson()*/, { cache: true }).then(function(resp) {
        console.log("$filter('json')(resp.data)"+$filter('json')(resp));
        console.log("sendResetEmail");
        if (resp.data.resultCode == "0001") {
          UtilsService.msgboxAlert("메일이 전송되었습니다.");
        } else if (resp.data.resultCode == "5001") {
          UtilsService.msgboxAlert("메일전송이 실패했습니다.");
        } else {
          UtilsService.msgboxAlert("오류가 발생했습니다.");
        }
        UtilsService.msgboxWaitClose(dlg);
        return resp.data.resultCode;
      },
      function(resp){
        console.log("sendResetEmail fail: "+$filter('json')(resp));
        UtilsService.msgboxAlert("오류가 발생했습니다.");
        return "9999";
      });
    },
  }

  return service;
})
