/***************************************************************************************************
 ***** This file is part of RestauRate. *****
 ***** *****
 ***** Copyright (C) 2014 HoldInArms *****
 ***** *****
 ***** This program is free software: you can redistribute it and/or modify it under the       *****
 ***** terms of the GNU General Public License as published by the Free Software Foundation,   *****
 ***** either version 3 of the License, or (at your option) any later version.                 *****
 ***** *****
 ***** This program is distributed in the hope that it will be useful, but WITHOUT ANY         *****
 ***** WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A         *****
 ***** PARTICULAR PURPOSE. See the GNU General Public License for more details.                *****
 ***** *****
 ***** You should have received a copy of the GNU General Public License along with this       *****
 ***** program. If not, see <http://www.gnu.org/licenses/>. *****
 ***************************************************************************************************/

var RestaurantBlacklist = angular.module('RestaurantBlacklist', [
    "RestaurantBlacklist.controllers",
    "RestaurantBlacklist.services",
    "RestaurantBlacklist.directives",
    "ui.router",
    "ui.bootstrap",
    "ui.bootstrap.datetimepicker"

]);

RestaurantBlacklist.config(function($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise("/home"),
    $stateProvider

    // --------------- PUBLIC ZONE --------------------            
    .state('public', {
        url: "",
        abstract: true,
        template: "<div ui-view></div>"
    })

    .state('public.home', {
        url: "/home",
        templateUrl: "assets/partials/home.html",
        controller: 'HomePageController'
    })

    .state('public.error', {
        url: "/error",
        templateUrl: "assets/partials/error.html"
    })

    // --------------- ADMIN ZONE --------------------       
    .state('admin', {
        url: "",
        abstract: true,
        templateUrl: "assets/partials/admin/index.html"
    })

    .state('admin.home', {
        url: "/admin",
        controller: 'AdminPage'
    })

    .state('admin.login', {
        url: "/admin/login",
        templateUrl: "assets/partials/admin/login.html",
        controller: 'AdminLoginPageController'
    })

    .state('admin.restaurants', {
        url: "/admin/restaurants",
        templateUrl: "assets/partials/admin/restaurants.html",
        controller: 'AdminRestaurantsPageController'
    })

    .state('admin.comments', {
        url: "/admin/comments/:restaurantId",
        templateUrl: "assets/partials/admin/comments.html",
        controller: 'AdminCommentsPageController'
    })

    .state('admin.new-admin', {
        url: "/admin/new-admin",
        templateUrl: "assets/partials/admin/new-admin.html",
        controller: 'AdminNewAdminPageController'
    })

    .state('admin.change_password', {
        url: "/admin/change-password",
        templateUrl: "assets/partials/admin/change_password.html",
        controller: 'AdminChangePasswordPageController'
    })

    .state('admin.logout', {
        url: "/admin/logout",
        controller: 'AdminLogoutController'
    });
});