          map = new OpenLayers.Map("demoMap");
          var mapnik = new OpenLayers.Layer.OSM();
          var fromProjection = new OpenLayers.Projection("EPSG:4326");   // Transform from WGS 1984
          var toProjection = new OpenLayers.Projection("EPSG:900913"); // to Spherical Mercator Projection
          var position = new OpenLayers.LonLat(4.4, 45.4333).transform(fromProjection, toProjection);

          var zoom = 13;
          var vectorLayer = new OpenLayers.Layer.Vector("Vectors");


          map.addLayer(mapnik);
          map.addLayer(vectorLayer);

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

          var p = new OpenLayers.LonLat(4.4, 45.4333).transform(fromProjection, toProjection)
          var point = new OpenLayers.Geometry.Point(p.lon,p.lat);
          var pointFeature = new OpenLayers.Feature.Vector(point, null, style_green);

          var pointList = [];
          pointList = [
             {lon:4.40300407409665,  lat: 45.431819810071644 },
             {lon:4.404205703735317, lat: 45.43142829856505  },
             {lon:4.405128383636406, lat: 45.431059371775355 },
             {lon:4.404785060882565, lat: 45.43058503378825  },
             {lon:4.4043237209320205,lat: 45.429990223376905 },
             {lon:4.405139112472495, lat: 45.42974175641238  },
             {lon:4.407016658782935, lat: 45.42967399250492  },
             {lon:4.407553100585829,lat:45.42984716677313},
            {lon:4.409430646896269,lat:45.429832108162394},
            {lon:4.4093609094619195,lat:45.430152102781264}
          ]

          function transformList(list){
               for(let i = 0; i < list.length; i++){
                    list[i] = new OpenLayers.LonLat( list[i].lon,list[i].lat).transform(fromProjection, toProjection)
                    list[i]=  new OpenLayers.Geometry.Point(list[i].lon,list[i].lat)
               }
          }

        transformList(pointList)
        
        var lineFeature = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.LineString(pointList), null, style_green);


          vectorLayer.addFeatures(lineFeature);
          var markers = new OpenLayers.Layer.Markers("Markers");
          map.addLayer(markers);
          markers.addMarker(new OpenLayers.Marker(position));
          markers.addMarker(new OpenLayers.Marker(position));

          map.setCenter(position, zoom);
          var opx
          var lon = document.getElementById("lon")
          var lat = document.getElementById("lat")

          var index = 0;
          map.events.register("click", map, function (e) {
              console.log(e)
              console.log(index)
               opx = map.getLonLatFromViewPortPx(e.xy);
              console.log(opx)

               markers.markers[index].moveTo(map.getLayerPxFromViewPortPx(e.xy));
               opx = opx.transform(toProjection, fromProjection)
              console.log(opx.toString())
               console.log(markers.markers[0])
            index = (index +1)%2
            console.log(index)

          });

          function test() {
               lon.value = "test"
               lat.value = "test"
          }