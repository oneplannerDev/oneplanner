angular.module('adminApp').service('TodayService', function($rootScope, $http, $filter,RESOURCES, UtilsService,$q) {
  var service = {
    getAllTodays: function(params) {
      console.log("getAllTodays params="+$filter('json')(params));

      //return $http.get('data/users.json', { cache: true }).then(function(resp) {
      return $http.post(RESOURCES.SERVER_URL+'/admin/today/list',$filter('json')(params), { cache: true }).then(function(resp) {
      //console.log("$filter('json')(resp.data.item)"+$filter('json')(resp.data));
        $rootScope.todaysParams = params;
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
    getToday: function(params) {
      console.log("getToday params="+$filter('json')(params));
      //return $http.get('data/users.json', { cache: true }).then(function(resp) {
      //return $http.post('http://localhost:8081/oneplanner/subscr/userinfo/get'
      return $http.post(RESOURCES.SERVER_URL+'/today/get',$filter('json')(params), { cache: true }).then(function(resp) {
        console.log("$filter('json')(resp.data)"+$filter('json')(resp.data));
        today = resp.data.item;
        today.todayP = UtilsService.strToDate(today.today);
        today.imageUrlFull = UtilsService.fullUrl(today.imageType, today.imageUrl);
        console.log("$filter('json')(resp.data)"+$filter('json')(today));

        return today;
      });
    },

    registerToday: function(today, upfile) {
      console.log("registerToday:"+$filter('json')(today));
      today.today = UtilsService.dateNoHypen(today.today);
      if (upfile == undefined) {
        return $http.post(RESOURCES.SERVER_URL+'/today/admin/register/json',$filter('json')(today), { cache: true }).then(function(resp) {
          console.log("$filter('json')(resp.data)"+$filter('json')(resp.data));
          return (resp.data.resultCode == "0001" || resp.data.resultCode == "0002");
        });
      } else {
        var upload = this.upload;
        return upload(RESOURCES.SERVER_URL+'/today/admin/register'
        , today, upfile).then(function(resp) {
          result = false;
          console.log("$filter('json')(resp.data)"+$filter('json')(resp.data));
          return (resp.data.resultCode == "0001" || resp.data.resultCode == "0002");
        });

      }
    },
    updateToday: function(today, upfile) {
      console.log("updateToday:"+$filter('json')(today));
      console.log("updateToday upfile:"+upfile);
      param = {}
      param.today = today.today;
      param.contSeq = today.contSeq;
      param.title = today.title;
      param.imageType = today.imageType;
      param.content = today.content;
      param.imageUrl = today.imageUrl;
      if (upfile == undefined) {
        return $http.post(RESOURCES.SERVER_URL+'/today/admin/update/json',$filter('json')(param), { cache: true }).then(function(resp) {
          console.log("$filter('json')(resp.data)"+$filter('json')(resp.data));
          return (resp.data.resultCode == "0001" || resp.data.resultCode == "0002");
        });
      } else {
        var upload = this.upload;
        return upload(RESOURCES.SERVER_URL+'/today/admin/update'
        , param, upfile).then(function(resp) {
          result = false;
          console.log("$filter('json')(resp.data)"+$filter('json')(resp.data));
          return (resp.data.resultCode == "0001" || resp.data.resultCode == "0002");
        });

      }
    },
    deleteToday: function(today, callback) {
      console.log("deleteToday:"+$filter('json')(today));
      param = {}
      param.today = today.today;
      param.contSeq = today.contSeq;
      UtilsService.msgboxOkCancel("삭제확인",today.today+"/"+today.contSeq+"번째 등록내역을 삭제하시겠습니까?"
            ,"확인","취소",
            function() {
              return $http.post(RESOURCES.SERVER_URL+'/today/admin/remove',$filter('json')(param), { cache: true }).then(function(resp) {
                console.log("$filter('json')(resp.data)"+$filter('json')(resp.data));
                if (resp.data.resultCode == "0001" || resp.data.resultCode == "0002")
                  callback();
              });
            },
            function() {}
      );
    },
    upload: function(urlStr, jsondata, uploadFile) {
      console.log("uploadFile:"+uploadFile);
      var upl = $http({
        method: 'POST',
        url: urlStr, //'http://jsonplaceholder.typicode.com/posts', // /api/upload
        headers: { 'Content-Type': (uploadFile != undefined)?undefined:'multipart/form-data' },
        transformRequest: function(data) {
          var formData = new FormData();
           //need to convert our json object to a string version of json otherwise
           // the browser will do a 'toString()' on the object which will result
           // in the value '[Object object]' on the server.
           formData.append('json', new Blob([angular.toJson(data.json)], {
              type: "application/json"
          }));
           //now add all of the assigned files
           if (data.upfile != undefined)
            formData.append("upfile", data.upfile);
            else
            console.log("upfile is not defined");
           return formData;
         },
         data: {
           upfile: uploadFile,
           json: jsondata
         }
      });
      handleSuccess = this.handleSuccess;
      handleError = this.handleError;
      return upl.then(handleSuccess, handleError);
    }, // End upload function
    handleError: function (response, data) {
      if (!angular.isObject(response.data) ||!response.data.message) {
        return ($q.reject("An unknown error occurred."));
      }
      return ($q.reject(response.data.message));
    },

    handleSucces: function(response) {
      return (response);
    }
  }

  return service;
})
