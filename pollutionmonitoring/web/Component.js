sap.ui.define([
	"sap/ui/core/UIComponent"
], function(UIComponent,Device,models) {
	"use strict";
	return UIComponent.extend("com.sap.hcp.samples.Component", {
		metadata: {
			manifest: "json"
		},
		/**
		 * The component is initialized by UI5 automatically during the startup of the app and calls the init method once.
		 * @public
		 * @override
		 */
		init: function() {
			// UIComponent.prototype.init.apply(this, arguments);
			// var oAppModel = new sap.ui.model.json.JSONModel();
			// 		jQuery.getJSON("/plants").done(function(mData) {
			// 			oAppModel.setData(mData);
			// 		});
			// 	//	this.setModel(oAppModel);
			// 		sap.ui.getCore().setModel(oAppModel, "userapi"); 
			
			// call the base component's init function
			UIComponent.prototype.init.apply(this, arguments);
			// set the device model
		//	this.setModel(models.createDeviceModel(), "device");
		}
	});
});