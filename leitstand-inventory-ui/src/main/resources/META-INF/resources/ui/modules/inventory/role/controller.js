/*
 * Copyright 2020 RtBrick Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
import {Controller,Menu} from '/ui/js/ui.js';
import {Roles,Role} from '/ui/modules/inventory/inventory.js';

const rolesController = function() {
	const roles = new Roles();
		return new Controller({resource:roles,
						viewModel:function(roles){
							return {"roles":roles};
						}});
};
	
const roleController = function() {
	const role = new Role();
	return new Controller({resource:role,
					 buttons:{
						"save":function(){
							 const settings = this.updateViewModel({
								 "role_name":this.input("role_name").value(),
								 "display_name":this.input("display_name").value(),
								 "manageable":this.input("manageable").isChecked(),
								 "plane":this.input("plane").value(),
								 "description":this.input("description").value()
							 });
							 role.saveSettings(this.location.params,
									 		   settings);
						},
						"remove-role":function(){
							role.remove(this.location.params);
						}
					},
					onSuccess:function(){
						this.navigate("roles.html");
					},
					onRemoved:function(){
						this.navigate("roles.html");
					}
				});
};

const addRoleController = function() {
	const roles = new Roles();
	return new Controller({resource:roles,
				 	 buttons:{
				 		 "save":function(){
				 			 const role = {
				 				"role_name":this.input("role_name").value(),
								"display_name":this.input("display_name").value(),
								"manageable":this.input("manageable").isChecked(),
								"plane":this.input("plane").value(),
								"description":this.input("description").value()
							};
							roles.addRole(this.location.params,
										  role);
						}
					 },
					 onSuccess:function(){
						this.navigate("roles.html");
					}
	});
};

const roleMenu =  {
	"master" : rolesController(),
	"details" : {
		"new-role.html":addRoleController(),
		"confirm-remove.html":roleController(),
		"role.html":roleController()
	} 
}

export const menu = new Menu({"roles.html" : roleMenu});
	
