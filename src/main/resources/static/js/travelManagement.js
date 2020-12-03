          //By Jeremy
          var list = document.getElementsByClassName("point")
          var pointList = [];
          var list1 = [];
          var newPath = []

         var testTM = "travel Management"
          //Init map
          map = new OpenLayers.Map("demoMap");
          var mapnik = new OpenLayers.Layer.OSM();
          var fromProjection = new OpenLayers.Projection("EPSG:4326");   // Transform from WGS 1984
          var toProjection = new OpenLayers.Projection("EPSG:900913"); // to Spherical Mercator Projection
          var position = new OpenLayers.LonLat(4.4, 45.4333).transform(fromProjection, toProjection);

          var zoom = 16;
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

          //transform point list in OpenLayers.Geometry.Point
          function transformList(list1){
              list2 = []
               for(let i = 0; i < list1.length; i++){
                    list2.push( {lon : parseFloat(list1[i].children[0].innerText), lat:parseFloat(list1[i].children[1].innerText)} )
               }
               for(let i = 0; i < list2.length; i++){
                    list2[i] = new OpenLayers.LonLat( list2[i].lon, list2[i].lat).transform(fromProjection, toProjection)
                    list2[i]=  new OpenLayers.Geometry.Point(list2[i].lon,list2[i].lat)
               }
               return list2
          }

       pointList =  transformList(list)
        
        var lineFeature = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.LineString(pointList), null, style_green);

        function showPath(){
          vectorLayer.eraseFeatures(lineFeature)
             list2 = []
             console.log("showPath",list1)
          for(let i = 0; i < list1.length; i++){
               list2.push( {lon : list1[i].longitude, lat: list1[i].latitude} )
          }
          for(let i = 0; i < list2.length; i++){
               list2[i] = new OpenLayers.LonLat( list2[i].lon, list2[i].lat).transform(fromProjection, toProjection)
               list2[i]=  new OpenLayers.Geometry.Point(list2[i].lon,list2[i].lat)
          }
          lineFeature = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.LineString(list2), null, style_green);
          
          vectorLayer.addFeatures(lineFeature)
        }

          vectorLayer.addFeatures(lineFeature)
          var markers = new OpenLayers.Layer.Markers("Markers");
          map.addLayer(markers);
          map.setCenter(position, zoom);

          var opx
          //var use by form
          var startLon = document.getElementById("startLon")
          var startLat = document.getElementById("startLat")
          var endLon = document.getElementById("endLon")
          var endLat = document.getElementById("endLat")

          //permit to add 2 markers
          map.events.register("click", map, async function (e) {
               if(markers.markers.length < 2){
                    opx = map.getLonLatFromViewPortPx(e.xy);
                    console.log(opx)
                    markers.addMarker(new OpenLayers.Marker(opx));
                    opx = opx.transform(toProjection, fromProjection)
                  if(markers.markers.length == 1){
                       startLon.value = opx.lon
                       startLat.value = opx.lat
                       
                    }
                    else{
                         endLon.value = opx.lon
                         endLat.value = opx.lat

                         let res = await fetch('/createPath', {
                              method: 'POST',
                              headers: { 'Content-Type': 'application/json' },
                              body: JSON.stringify({startLon:startLon.value, startLat:startLat.value,endLon :endLon.value,endLat:endLat.value })
                         })
                         body = await res.json()
                         console.log("RESPONSE",body.points)
                         list1 = body.points
                         newPath = body
                         console.log("RESPONSE body",body)
                         showPath()
                    }

                    console.log(opx)
               }
          });