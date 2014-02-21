//base url for api calls
var baseUrl = "/api/";
angular.module('RestaurantBlacklist.services', [])

/**
 * CredentialHolder factory
 * Used for, saving/geting login tokens
 */
.factory('CredentialHolder', function() {
	var holder = {
		credentials: null
	};

	holder.load = function() {
		var c = angular.fromJson(sessionStorage.credentials);
		holder.credentials = c ? c : null;
	};

	holder.save = function() {
		sessionStorage.credentials = angular.toJson(holder.credentials);
	};

	holder.isLoggedIn = function() {
		return holder.credentials !== null;
	};

	holder.getAuthToken = function() {
		return holder.credentials ? holder.credentials.key : null;
	};

	// logut function removes also from session storage
	holder.logout = function() {
		holder.credentials = null;
		sessionStorage.credentials = null;
	};

	holder.load();

	return holder;
})

.factory('RestaurantService', ['$http',
	function($http) {
		var api = {};

		/**
		 * Set the error function.
		 *
		 * @param errorFunction The error function.
		 */
		api.setErrorFunction = function(errorFunction) {
			api.errorFunction = errorFunction;
		};

		/**
		 * Get all restaurant list.
		 *
		 */

		api.getRestaurants = function(restaurants, from, to, filterText, orderby, direction, successFunction) {
			var query = "";
			if (orderby) {
				query += "&orderby=" + orderby;
			}

			if (direction) {
				query += "&direction=" + direction;
			}

			if (filterText) {
				query += "&filtertext=" + filterText;
			}

			$http({
				url: baseUrl + 'restaurant/list?from=' + from + '&to=' + to + query,
				method: 'GET'
			}).success(function(data, status, headers, config) {
				angular.copy(data, restaurants);
				if (successFunction) {
					successFunction();
				}
			}).error(function(data, status, headers, config) {
				api.errorFunction();
			});
		};

		api.getAllRestaurants = function(restaurants, successFunction) {

			$http({
				url: baseUrl + 'restaurant/all',
				method: 'GET'
			}).success(function(data, status, headers, config) {
				angular.copy(data, restaurants);
				if (successFunction) {
					successFunction();
				}
			}).error(function(data, status, headers, config) {
				api.errorFunction();
			});
		};

		api.newRestaurant = function(comment, successFunction) {
			$http({
				url: baseUrl + 'comment/saveWithNewRestaurant',
				method: 'POST',
				data: comment
			}).success(function(data, status, headers, config) {
				successFunction();
			}).error(function(data, status, headers, config) {
				api.errorFunction();
			});
		};

		api.getCommentsById = function(restaurantId, comments, from, to, successFunction) {
			var query = "from=" + from + "&to=" + to;

			$http({
				url: baseUrl + 'comment/list/' + restaurantId + "?" + query,
				method: 'GET',
			}).success(function(data, status, headers, config) {
				angular.copy(data, comments);
				successFunction();
			}).error(function(data, status, headers, config) {
				api.errorFunction();
			});
		};

		api.newCommentForRestaurant = function(postData, successFunction) {
			$http({
				url: baseUrl + 'comment/saveWithoutNewRestaurant',
				method: 'POST',
				data: postData
			}).success(function(data, status, headers, config) {
				successFunction();
			}).error(function(data, status, headers, config) {
				api.errorFunction();
			});
		};

		return api;
	}
])

.factory('AdminLoginService', ['$http', 'CredentialHolder',
	function($http, CredentialHolder) {
		var api = {};

		/**
		 * Set the error function.
		 *
		 * @param errorFunction The error function.
		 */
		api.setErrorFunction = function(errorFunction) {
			api.errorFunction = errorFunction;
		};


		api.login = function(username, password, successFunction, wrongLoginFunction) {
			$http({
				url: baseUrl + 'admin/login/' + username + '/' + password,
				method: 'GET',
			}).success(function(data, status, headers, config) {
				if (data) {
					var authData = {
						key: data
					};

					CredentialHolder.credentials = authData;
					CredentialHolder.save();

					successFunction();
				} else {
					wrongLoginFunction();
				}
			}).error(function(data, status, headers, config) {
				api.errorFunction();
			});
		};

		return api;
	}
])



.factory('AdminRestaurantService', ['$http',
	function($http) {
		var api = {};

		/**
		 * Set the error function.
		 *
		 * @param errorFunction The error function.
		 */
		api.setErrorFunction = function(errorFunction) {
			api.errorFunction = errorFunction;
		};

		api.hide = function(id, successFunction) {
			$http({
				url: baseUrl + 'restaurant/delete/' + id,
				method: 'POST'
			}).success(function(data, status, headers, config) {
				successFunction();
			}).error(function(data, status, headers, config) {
				api.errorFunction();
			});
		};

		api.reInstate = function(id, successFunction) {
			$http({
				url: baseUrl + 'restaurant/reinstate/' + id,
				method: 'POST'
			}).success(function(data, status, headers, config) {
				successFunction();
			}).error(function(data, status, headers, config) {
				api.errorFunction();
			});
		};


		return api;
	}
])


.factory('AdminCommentService', ['$http',
	function($http) {
		var api = {};

		/**
		 * Set the error function.
		 *
		 * @param errorFunction The error function.
		 */
		api.setErrorFunction = function(errorFunction) {
			api.errorFunction = errorFunction;
		};

		api.getCommentsById = function(comments, restaurantId, from, to, orderby, direction, successFunction) {
			var query = "?from=" + from + "&to=" + to;
			if (restaurantId) {
				query += "&restaurantid=" + restaurantId;
			}
			if (orderby) {
				query += "&orderby=" + orderby;
			}

			if (direction) {
				query += "&direction=" + direction;
			}

			$http({
				url: baseUrl + 'comment/adminlist' + query,
				method: 'GET'
			}).success(function(data, status, headers, config) {
				angular.copy(data, comments);
				if (successFunction) {
					successFunction();
				}
			}).error(function(data, status, headers, config) {
				api.errorFunction();
			});
		};

		api.hide = function(id, successFunction) {
			$http({
				url: baseUrl + 'comment/delete/' + id,
				method: 'POST'
			}).success(function(data, status, headers, config) {
				successFunction();
			}).error(function(data, status, headers, config) {
				api.errorFunction();
			});
		};

		api.reInstate = function(id, successFunction) {
			$http({
				url: baseUrl + 'comment/reinstate/' + id,
				method: 'POST'
			}).success(function(data, status, headers, config) {
				successFunction();
			}).error(function(data, status, headers, config) {
				api.errorFunction();
			});
		};

		return api;
	}
])

.config(['$httpProvider',
	function($httpProvider) {
		$httpProvider.interceptors.push(function(CredentialHolder) {
			var requestHandler = function(config) {
				// example Bearer 4ac4fb7d-cdcf-4e5e-bb2f-a6b845d50e0d				
				config.headers.Authorization = 'Bearer ' + CredentialHolder.getAuthToken();
				return config;
			};

			return {
				'request': requestHandler
			};
		});
	}
]);