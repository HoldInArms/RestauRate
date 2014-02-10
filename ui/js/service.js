//base url for api calls
var baseUrl = "/api/";
angular.module('RestaurantBlacklist.services', [])

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

.factory('AdminLoginService', ['$http',
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


		api.login = function(username, password, successFunction, wrongLoginFunction) {
			$http({
				url: baseUrl + 'admin/login/' + username + '/' + password,
				method: 'GET',
			}).success(function(data, status, headers, config) {
				if (data) {
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