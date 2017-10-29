angular.module('adminApp').service('UtilsService', function($state,$q,$window,RESOURCES) {
  var service = {
    toLocalDate : function(date) {
	     var local = new Date(date);
	      local.setMinutes(date.getMinutes() - date.getTimezoneOffset());
	       return local.toJSON();
    },
    todayShort: function() {
    	var local = new Date();
    	local.setMinutes(local.getMinutes() - local.getTimezoneOffset());
    	return local.toJSON().slice(0, 10).replace('-','').replace('-','');
    },
    todayHypen: function() {
    	var local = new Date();
    	local.setMinutes(local.getMinutes() - local.getTimezoneOffset());
    	return local.toJSON().slice(0, 10);
    },
    toJSONLocalDate: function(date) {
    	var local = new Date(date);
    	local.setMinutes(date.getMinutes() - date.getTimezoneOffset());
    	return local.toJSON().slice(0, 10);
    },
    dateHypen: function(strDate) {
      if (strDate == null) return null;
        yyyy= strDate.substring(0,4);
        mm  = strDate.substring(4,6);
        dd  = strDate.substring(6,8);
      return yyyy+"-"+mm+"-"+dd;
    },
    dateNoHypen: function(strDate) {
      if (strDate == null) return null;
      return strDate.replace('-','').replace('-','');
    },
    strToDate: function(strDate) {
      //console.log("strDate:"+strDate);
      if (strDate == null) return null;
        yyyy= strDate.substring(0,4);
        mm  = parseInt(strDate.substring(4,6), 10)-1;
        dd  = strDate.substring(6,8);
      if (strDate.length == 14) {
        hh  = strDate.substring(8,10);
        mi  = strDate.substring(10,12);
        ss  = strDate.substring(12,14);
      } else {
        hh = "00";
        mi = "00";
        ss = "00";
      }
      var toPrintDate = this.toJSONLocalDate;
      var temp = new Date(yyyy,mm,dd,hh,mi,ss);
      //console.log(temp);
      result =toPrintDate(new Date(yyyy,mm,dd,hh,mi,ss));
      //console.log("result:"+result );
      return toPrintDate(new Date(yyyy,mm,dd,hh,mi,ss));
    },

    fullUrl: function(type, url) {
      if (type == 'I') {
        return (url==null)?null:RESOURCES.IMAGE_URL+url;
      } else {
        return (url==null)?null:url;
      }
    },
    mailStatus: function(authMode, authYn) {
      if (authMode == 'A') {
        return "인증메일전송";
      } else if (authMode == 'Y') {
        return "인증완료";
      } else if (authMode == 'R') {
        return "비번리셋메일전송";
      } else if (authMode == 'S') {
        return "비번리셋완료";
      }

    },
    authBtnDisable: function(authMode)  {
      return (authMode == 'Y' || authMode == 'S' || authMode == 'R');
    },
    resetBtnDisable: function(authMode)  {
      return (authMode != 'Y' && authMode != 'S' && authMode != 'R');
    },
    testPasswdPattern:  function(str, minLen, maxLen) {
      // ok Upper, lower, Number, length >= 9
      // warning Upper, lower, Number, length < 9
      // error length > 0
      level = 0; // -1 : error, 0 : init, 1 : warning, 2 : ok

      //"(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}"
      var pat = new RegExp("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])");
      // var patMinLength = new RegExp(".{"+minLen+",}/");
      // var patMaxLength = new RegExp(".{"+maxLen+",}/");
      if (str == undefined || str.length == 0)  {
        console.log("string length = 0");
        return 0;
      }
      //여기서는 사용하지 않음
      /*
      if (!pat.test(str)) {
        console.log("invalid pattern str:"+str);
        return -1;
      }
      */

      //if (!patHasLength.test(str)) {
      if (str.length < minLen) {
        console.log("less than "+minLen + " str:"+str);
        return -1;
      }
      if (str.length < maxLen) {
        console.log("less than "+maxLen + " str:"+str);
        return 2;
      }
      console.log("ok pattern str:"+str);
      return 2;
    },

    //0: 초기화, -1: 조건안맞음. 2: 조건맞츰
    testUserIdPattern: function(str, minLen) {
      var pat = new RegExp("^[_A-z0-9]{"+minLen+",}$");
      if (str == undefined || str.length == 0)  {
        console.log("string length = 0");
        return 0;
      }
      if (!pat.test(str)) {
        console.log("invalid pattern str:"+str);
        return -1;
      }
      console.log("ok pattern str:"+str);
      return 2;
    },

    testEmailPattern: function(str) {
      var pat = new RegExp("[A-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$");

      if (str == undefined || str.length == 0)  {
        console.log("string length = 0");
        return 0;
      }
      if (!pat.test(str)) {
        console.log("invalid pattern str:"+str);
        return -1;
      }
      console.log("ok pattern str:"+str);
      return 2;
    },

    roleName: function(str) {
      if (str == 'S') {
        return "슈퍼유저";
      } else if (str == 'N') {
        return "관리자";
      } else if (str == 'O') {
        return "일반사용자";
      } else {
        return "기타사용자";
      }
    },

    userType: function(str) {
      if (str == 'G') {
          return "Google";
      } else {
          return "이메일직접";
      }
    },

    range: function(start, count) {
      return Array.apply(0, Array(count))
        .map(function (element, index) {
          return index + start;
      });
    },
    msgboxOkCancel: function(titleStr, content, okStr, cancelStr,okfunc, cancelfunc) {
      bootbox.confirm({
          title: titleStr,
          message: content,
          buttons: {
            confirm: {
                label: '<i class="fa fa-check"></i>'+cancelStr
            },
            cancel: {
                label: '<i class="fa fa-times"></i>'+okStr
            }
          },
          callback: function (result) {
              if (!result) {
                 okfunc();
              } else {
                cancelfunc();
              }
              console.log('This was logged in the callback: ' + result);
          }
      });

    },
    msgboxConfirm: function(message, callback) {
      bootbox.confirm(message, callback);
    },
    msgboxAlert: function(message, callback) {
      bootbox.alert(message, callback);
    },
    msgboxWait: function() {
      return bootbox.dialog({
        message: '<span class="glyphicon glyphicon-refresh glyphicon-refresh-animate"></span><p class="text-center">Please wait...</p>',
        closeButton: false
      });
    },
    msgboxWaitClose: function(dlg) {
      dlg.modal('hide');
    },
    focusElement: function(id) {
      var element = $window.document.getElementById(id);
      if (element) {
        console.log("element.focus()");
        element.focus();
      }
    },

    isImage: function(src) {
        var deferred = $q.defer();
        if (!src.includes("http://") && !src.includes("https://")) {
          deferred.reject("no image");
        };

        var image = new Image();
        image.onerror = function() {
            deferred.resolve(false);
        };
        image.onload = function() {
            deferred.resolve(true);
        };
        image.src = src;

        return deferred.promise;
    },
  }
  return service;
})
