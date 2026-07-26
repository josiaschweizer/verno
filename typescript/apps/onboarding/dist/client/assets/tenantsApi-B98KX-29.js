import{c as a}from"./createLucideIcon-BJZQ6W6H.js";import{a as t}from"./client-CAtUlqDr.js";/**
 * @license lucide-react v0.563.0 - ISC
 *
 * This source code is licensed under the ISC license.
 * See the LICENSE file in the root directory of this source tree.
 */const o=[["path",{d:"M18 6 6 18",key:"1bl5f8"}],["path",{d:"m6 6 12 12",key:"d8bk6v"}]],p=a("x",o),u={createTenant(e){return t.request({method:"POST",path:"/api/tenants",body:e})},getCountOfTenants(){return t.request({method:"GET",path:"/api/tenants/count"})},getTotalMemberCount(){return t.request({method:"GET",path:"/api/application/memberCount"})},getTotalCourseCount(){return t.request({method:"GET",path:"/api/application/courseCount"})}};export{p as X,u as t};
