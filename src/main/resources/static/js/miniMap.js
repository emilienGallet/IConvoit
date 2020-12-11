          //By Jeremy
          function showPath(miniList){
         

          //Init map
          minimap = new OpenLayers.Map("miniMap");
          var minimapnik = new OpenLayers.Layer.OSM();
          var fromProjection = new OpenLayers.Projection("EPSG:4326");   // Transform from WGS 1984
          var toProjection = new OpenLayers.Projection("EPSG:900913"); // to Spherical Mercator Projection
          var position = new OpenLayers.LonLat(4.4, 45.4333).transform(fromProjection, toProjection);

          var zoom = 15;
          var vectorLayer = new OpenLayers.Layer.Vector("Vectors");


          minimap.addLayer(minimapnik);
          minimap.addLayer(vectorLayer);

          var layer_style = OpenLayers.Util.extend({}, OpenLayers.Feature.Vector.style['default']);
          layer_style.fillOpacity = 0.2;
          layer_style.graphicOpacity = 1;


          var style_green = {
               strokeColor: "#00FF00",
               strokeWidth: 3,
               strokeDashstyle: "solid",
               pointRadius: 6,
               pointerEvents: "visiblePainted",
               title: "this is a green line"
          };

             list2 = []
             console.log("showPath",miniList)
          for(let i = 0; i < miniList.length; i++){
               list2.push( {lon : miniList[i].longitude, lat: miniList[i].latitude} )
          }
          position = new OpenLayers.LonLat( list2[0].lon, list2[0].lat).transform(fromProjection, toProjection)
          for(let i = 0; i < list2.length; i++){
               list2[i] = new OpenLayers.LonLat( list2[i].lon, list2[i].lat).transform(fromProjection, toProjection)
               list2[i]=  new OpenLayers.Geometry.Point(list2[i].lon,list2[i].lat)
          }
          lineFeature = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.LineString(list2), null, style_green);
          vectorLayer.addFeatures(lineFeature)
          minimap.setCenter(position, zoom);
        }

         

       