sap.ui.define([
	"sap/ui/core/mvc/Controller", "sap/viz/ui5/controls/VizFrame",  "sap/viz/ui5/data/FlattenedDataset",  "sap/viz/ui5/controls/common/feeds/FeedItem"
], function(Controller, VizFrame, FlattenedDataset, FeedItem) {
	"use strict";
	return Controller.extend("com.sap.hcp.samples.controller.App", {
		onInit: function() {
			var rootPage = this.getView().byId("rootPage");
			var plantDataModel = new sap.ui.model.json.JSONModel();
			jQuery.getJSON("/pollutionmonitoring/api/v1/plantdata/").done(function(mData) {
				plantDataModel.setData(mData);
			});
			rootPage.setModel(plantDataModel,"plantsDetail");
			
			var polloutionDataModel = new sap.ui.model.json.JSONModel();
			jQuery.getJSON("/pollutionmonitoring/api/v1/pollutiondata/").done(function(mData) {
			polloutionDataModel.setData(mData);
			var plantsPollutionWeeklyData = polloutionDataModel.getProperty("/plantsPollutionWeeklyData");
			var len = Object.keys(plantsPollutionWeeklyData).length;
				for (var i = 0; i < len; i++) {
					var panel = new sap.m.Panel("myPanel" + i, {
						headerText: "{plantsDetail>/plants/0/" + i + "/plantName}" + " ," + "{plantsDetail>/plants/0/" + i + "/location}",
						content: [
							new sap.m.Label({
								text: "Plant Area " + "{plantsDetail>/plants/0/" + i + "/plantArea}"+" ,"
							}),
							new sap.m.Label({
								text: "Crude Oil Capacity " + "{plantsDetail>/plants/0/" + i + "/crudeOilCapacity}"+" ,"
							}),
							new sap.m.Label({
								text: "Number of workers " + "{plantsDetail>/plants/0/" + i + "/numberOfWorkers}"
							}),
							new VizFrame("myViz" + i, {
									dataset: new FlattenedDataset({
										dimensions: [{
											name: 'Date',
											value: {
												path: "plant/dateField"
											}
										}],
										measures: [{
											name: 'Plant O3',
											value: {
												path: "plant/o3"
											}
										}, {
											name: 'City O3',
											value: {
												path: "cityOzoneLevel"
											}
										}],
										data: {
											path: "pollutionData>/plantsPollutionWeeklyData/" + i + "/"
										}
									}),
									vizType: "line",
									width:"100%",
									vizProperties: {
										plotArea: {
											colorPalette: d3.scale.category20().range()
										},
										title: {
											alignment: "left",
											visible: true,
											text: "Comparision of Ozone levels by Plant and City"
										}

									},
									feeds: [
										new FeedItem({
											'uid': "valueAxis",
											'type': "Measure",
											'values': ["Plant O3"]
										}),
										new FeedItem({
											'uid': "valueAxis",
											'type': "Measure",
											'values': ["City O3"]
										}),
										new FeedItem({
											'uid': "categoryAxis",
											'type': "Dimension",
											'values': ["Date"]
										})

									]

							
							})
						]
					});
					rootPage.addContent(panel);
				}
			});
				rootPage.setModel(polloutionDataModel, "pollutionData");
		}
	});
});