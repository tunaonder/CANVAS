INSERT INTO PROJECT(projectname, mapname, publicProject, user_id)
VALUES ('Fairfax2', 'fairfax2.png', 0, 1);

insert into simulationmodel(modeldata, project_id)
values('[{"type":"EnterPoint","objectId":"s1","x":0.0018845582671031728,"y":0.7729809662770265,"nextId":"s2","minTime":"8","maxTime":"15"},{"type":"Standart","objectId":"s2","x":0.06003032967561249,"y":0.7800004768384358,"nextId":"s3","prevId":"s1"},{"type":"TrafficLight","objectId":"s3","x":0.1283277042531522,"y":0.7807804224563701,"nextId":"s4","prevId":"s2","greenStartTime":"0","greenDuration":"10","redDuration":"20"},{"type":"Fork","objectId":"s4","x":0.1518950509151378,"y":0.7800004768384358,"nextId":"s43","prevId":"s3","alternativeNextId":"s5","newPathProbability":"70"},{"type":"Merge","objectId":"s5","x":0.15629388456303353,"y":0.7659614557156175,"nextId":"s6","prevId":"s4","alternativePrevId":"s19"},{"type":"Standart","objectId":"s6","x":0.15241749980246622,"y":0.6427300480819882,"nextId":"s7","prevId":"s5"},{"type":"TrafficLight","objectId":"s7","x":0.14983324329542133,"y":0.5296379334815057,"nextId":"s8","prevId":"s6","greenStartTime":"10","greenDuration":"10","redDuration":"20"},{"type":"Fork","objectId":"s8","x":0.14789505091513777,"y":0.510919238651081,"nextId":"s13","prevId":"s7","alternativeNextId":"s9","newPathProbability":"30"},{"type":"Merge","objectId":"s9","x":0.14854111504189899,"y":0.45710299101361007,"nextId":"s10","prevId":"s8","alternativePrevId":"s73"},{"type":"Standart","objectId":"s10","x":0.14337260202780916,"y":0.25197729349687303,"nextId":"s11","prevId":"s9"},{"type":"Standart","objectId":"s11","x":0.13949621726724193,"y":0.07726947507957604,"nextId":"s12","prevId":"s10"},{"type":"ExitPoint","objectId":"s12","x":0.12140642171792791,"y":0.0023946957578772792,"prevId":"s11"},{"type":"Merge","objectId":"s13","x":0.13174344774610733,"y":0.4968802175282625,"nextId":"s14","prevId":"s8","alternativePrevId":"s112"},{"type":"ExitPoint","objectId":"s14","x":0.0005924300135807726,"y":0.5,"prevId":"s13"},{"type":"EnterPoint","objectId":"s15","x":0.13497376837991343,"y":0.9976053042421228,"nextId":"s16","minTime":"5","maxTime":"10"},{"type":"Fork","objectId":"s16","x":0.1453107944080929,"y":0.9718670988502888,"nextId":"s22","prevId":"s15","alternativeNextId":"s17","newPathProbability":"70"},{"type":"Standart","objectId":"s17","x":0.14660292266161531,"y":0.9320898723356363,"nextId":"s18","prevId":"s16"},{"type":"TrafficLight","objectId":"s18","x":0.15564782043627232,"y":0.8439560175090535,"nextId":"s19","prevId":"s17","greenStartTime":"10","greenDuration":"10","redDuration":"20"},{"type":"Fork","objectId":"s19","x":0.15435569218274983,"y":0.8228974858248258,"nextId":"s5","prevId":"s18","alternativeNextId":"s20","newPathProbability":"60"},{"type":"Merge","objectId":"s20","x":0.13368164012639092,"y":0.8104183559378758,"nextId":"s21","prevId":"s19","alternativePrevId":"s65"},{"type":"ExitPoint","objectId":"s21","x":0.0005924300135807726,"y":0.8104183559378758,"prevId":"s20"},{"type":"Fork","objectId":"s22","x":0.15823207694331717,"y":0.9383294372791111,"nextId":"s41","prevId":"s16","alternativeNextId":"s23","newPathProbability":"50"},{"type":"Standart","objectId":"s23","x":0.16017026932360082,"y":0.8977722651465244,"nextId":"s24","prevId":"s22"},{"type":"TrafficLight","objectId":"s24","x":0.16340058995740683,"y":0.8439560175090535,"nextId":"s25","prevId":"s23","greenStartTime":"10","greenDuration":"10","redDuration":"20"},{"type":"Standart","objectId":"s25","x":0.16469271821092937,"y":0.7667414013335516,"nextId":"s26","prevId":"s24"},{"type":"Standart","objectId":"s26","x":0.1608163334503621,"y":0.6427300480819881,"nextId":"s27","prevId":"s25"},{"type":"TrafficLight","objectId":"s27","x":0.1588781410700784,"y":0.528078042245637,"nextId":"s28","prevId":"s26","greenStartTime":"10","greenDuration":"10","redDuration":"20"},{"type":"Merge","objectId":"s28","x":0.15564782043627237,"y":0.45944282786741325,"nextId":"s29","prevId":"s27","alternativePrevId":"s114"},{"type":"Standart","objectId":"s29","x":0.15177143567570503,"y":0.251977293496873,"nextId":"s30","prevId":"s28"},{"type":"Standart","objectId":"s30","x":0.14854111504189893,"y":0.07570958384370727,"nextId":"s31","prevId":"s29"},{"type":"ExitPoint","objectId":"s31","x":0.1330355759996298,"y":0.0023946957578772792,"prevId":"s30"},{"type":"EnterPoint","objectId":"s32","x":0.000592430013580672,"y":0.7870199873998451,"nextId":"s33","minTime":"8","maxTime":"15"},{"type":"Standart","objectId":"s33","x":0.058092137295328944,"y":0.7901397698715825,"nextId":"s34","prevId":"s32"},{"type":"TrafficLight","objectId":"s34","x":0.13432770425315227,"y":0.7916996611074513,"nextId":"s35","prevId":"s33","greenStartTime":"0","greenDuration":"10","redDuration":"20"},{"type":"Merge","objectId":"s35","x":0.2041026299433634,"y":0.7948194435791888,"nextId":"s36","prevId":"s34","alternativePrevId":"s42"},{"type":"Fork","objectId":"s36","x":0.33008513466180045,"y":0.7924796067253856,"nextId":"s207","prevId":"s35","alternativeNextId":"s37","newPathProbability":"60"},{"type":"Standart","objectId":"s37","x":0.3778938800421304,"y":0.7948194435791888,"nextId":"s38","prevId":"s36"},{"type":"TrafficLight","objectId":"s38","x":0.448960933985864,"y":0.7963793348150575,"nextId":"s39","prevId":"s37","greenStartTime":"20","greenDuration":"10","redDuration":"30"},{"type":"Standart","objectId":"s39","x":0.48320233270420837,"y":0.8345966700938412,"nextId":"s40","prevId":"s38"},{"type":"ExitPoint","objectId":"s40","x":0.48255626857744716,"y":0.9976053042421228,"prevId":"s39"},{"type":"Standart","objectId":"s41","x":0.16921516709825787,"y":0.8993321563823932,"nextId":"s42","prevId":"s22"},{"type":"TrafficLight","objectId":"s42","x":0.17309155185882513,"y":0.8439560175090535,"nextId":"s35","prevId":"s41","greenStartTime":"20","greenDuration":"10","redDuration":"20"},{"type":"Merge","objectId":"s43","x":0.20410262994336345,"y":0.7862400417819106,"nextId":"s44","prevId":"s4","alternativePrevId":"s105"},{"type":"Standart","objectId":"s44","x":0.3365457759294126,"y":0.7792205312205015,"nextId":"s45","prevId":"s43"},{"type":"TrafficLight","objectId":"s45","x":0.4432530622393865,"y":0.7768806943666983,"nextId":"s46","prevId":"s44","greenStartTime":"0","greenDuration":"10","redDuration":"30"},{"type":"Fork","objectId":"s46","x":0.4648204089013719,"y":0.776100748748764,"nextId":"s195","prevId":"s45","alternativeNextId":"s47","newPathProbability":"70"},{"type":"Merge","objectId":"s47","x":0.47092711429574535,"y":0.7612817820080111,"nextId":"s48","prevId":"s46","alternativePrevId":"s236"},{"type":"Fork","objectId":"s48","x":0.4676967936619393,"y":0.655209177968938,"nextId":"s256","prevId":"s47","alternativeNextId":"s49","newPathProbability":"50"},{"type":"TrafficLight","objectId":"s49","x":0.4664046654084168,"y":0.5241783141559652,"nextId":"s50","prevId":"s48","greenStartTime":"10","greenDuration":"10","redDuration":"20"},{"type":"Fork","objectId":"s50","x":0.4664046654084168,"y":0.5054596193255406,"nextId":"s109","prevId":"s49","alternativeNextId":"s51","newPathProbability":"30"},{"type":"Merge","objectId":"s51","x":0.4696349860422228,"y":0.45710299101361007,"nextId":"s52","prevId":"s50","alternativePrevId":"s78"},{"type":"Standart","objectId":"s52","x":0.4676967936619393,"y":0.3096932692240157,"nextId":"s53","prevId":"s51"},{"type":"TrafficLight","objectId":"s53","x":0.46511253715489437,"y":0.21843963192569554,"nextId":"s54","prevId":"s52","greenStartTime":"10","greenDuration":"10","redDuration":"30"},{"type":"Fork","objectId":"s54","x":0.46446647302813326,"y":0.20050088271320515,"nextId":"s159","prevId":"s53","alternativeNextId":"s55","newPathProbability":"20"},{"type":"Merge","objectId":"s55","x":0.46446647302813326,"y":0.13420550518878446,"nextId":"s56","prevId":"s54","alternativePrevId":"s143"},{"type":"ExitPoint","objectId":"s56","x":0.45865189588728217,"y":0.0023946957578772792,"prevId":"s55"},{"type":"EnterPoint","objectId":"s57","x":0.4664046654084168,"y":0.9976053042421226,"nextId":"s58","minTime":"10","maxTime":"20"},{"type":"Standart","objectId":"s58","x":0.46511253715489437,"y":0.9203906880666208,"nextId":"s60","prevId":"s57"},{"type":"TrafficLight","objectId":"s60","x":0.46511253715489437,"y":0.8400562894193817,"nextId":"s61","prevId":"s58","greenStartTime":"10","greenDuration":"10","redDuration":"30"},{"type":"Standart","objectId":"s61","x":0.46575860128165564,"y":0.8228974858248258,"nextId":"s62","prevId":"s60"},{"type":"Merge","objectId":"s62","x":0.4457306133520579,"y":0.812758192791679,"nextId":"s63","prevId":"s61","alternativePrevId":"s234"},{"type":"Standart","objectId":"s63","x":0.34946705846463677,"y":0.8143180840275477,"nextId":"s64","prevId":"s62"},{"type":"TrafficLight","objectId":"s64","x":0.22231604073211014,"y":0.8189977577351538,"nextId":"s65","prevId":"s63","greenStartTime":"0","greenDuration":"10","redDuration":"20"},{"type":"Fork","objectId":"s65","x":0.19958018105603495,"y":0.8197777033530882,"nextId":"s20","prevId":"s64","alternativeNextId":"s67","newPathProbability":"60"},{"type":"Merge","objectId":"s67","x":0.1840746420137657,"y":0.8462958543628566,"nextId":"s68","prevId":"s65","alternativePrevId":"s105"},{"type":"Standart","objectId":"s68","x":0.17179942360530273,"y":0.9367695460432425,"nextId":"s69","prevId":"s67"},{"type":"ExitPoint","objectId":"s69","x":0.1537096280559886,"y":0.9976053042421228,"prevId":"s68"},{"type":"EnterPoint","objectId":"s70","x":0.000592430013580672,"y":0.4742617946081661,"nextId":"s71","minTime":"10","maxTime":"15"},{"type":"Standart","objectId":"s71","x":0.07359767633759795,"y":0.4773815770799035,"nextId":"s72","prevId":"s70"},{"type":"TrafficLight","objectId":"s72","x":0.1225131271123013,"y":0.4773815770799035,"nextId":"s73","prevId":"s71","greenStartTime":"0","greenDuration":"10","redDuration":"20"},{"type":"Fork","objectId":"s73","x":0.14208047377428676,"y":0.4733620665184942,"nextId":"s74","prevId":"s72","alternativeNextId":"s9","newPathProbability":"50"},{"type":"Merge","objectId":"s74","x":0.19053528328137795,"y":0.4766016314619691,"nextId":"s75","prevId":"s73","alternativePrevId":"s101"},{"type":"Standart","objectId":"s75","x":0.29519767181669476,"y":0.4773815770799035,"nextId":"s76","prevId":"s74"},{"type":"Fork","objectId":"s76","x":0.3746635594083243,"y":0.4773815770799035,"nextId":"s267","prevId":"s75","alternativeNextId":"s77","newPathProbability":"30"},{"type":"TrafficLight","objectId":"s77","x":0.44137667747881915,"y":0.47582168584403484,"nextId":"s78","prevId":"s76","greenStartTime":"0","greenDuration":"10","redDuration":"20"},{"type":"Fork","objectId":"s78","x":0.4612361523943271,"y":0.4742617946081661,"nextId":"s79","prevId":"s77","alternativeNextId":"s51","newPathProbability":"50"},{"type":"Merge","objectId":"s79","x":0.5038763847605674,"y":0.4750417402261004,"nextId":"s80","prevId":"s78","alternativePrevId":"s240"},{"type":"Fork","objectId":"s80","x":0.6221061199578697,"y":0.4734818489902317,"nextId":"s205","prevId":"s79","alternativeNextId":"s81","newPathProbability":"50"},{"type":"TrafficLight","objectId":"s81","x":0.74519533007068,"y":0.4688021752826255,"nextId":"s82","prevId":"s80","greenStartTime":"0","greenDuration":"10","redDuration":"30"},{"type":"Fork","objectId":"s82","x":0.770054804986188,"y":0.46568239281088813,"nextId":"s201","prevId":"s81","alternativeNextId":"s83","newPathProbability":"25"},{"type":"Merge","objectId":"s83","x":0.833369089408787,"y":0.46490244719295376,"nextId":"s84","prevId":"s82","alternativePrevId":"s214"},{"type":"ExitPoint","objectId":"s84","x":0.998761505859658,"y":0.46100271910328194,"prevId":"s83"},{"type":"EnterPoint","objectId":"s85","x":0.14208047377428676,"y":0.001614750139943014,"nextId":"s86","minTime":"5","maxTime":"15"},{"type":"Standart","objectId":"s86","x":0.15500175630951107,"y":0.07648952946164166,"nextId":"s87","prevId":"s85"},{"type":"Standart","objectId":"s87","x":0.1608163334503621,"y":0.2504174022610043,"nextId":"s88","prevId":"s86"},{"type":"TrafficLight","objectId":"s88","x":0.16404665408416808,"y":0.4485235892163321,"nextId":"s89","prevId":"s87","greenStartTime":"10","greenDuration":"10","redDuration":"20"},{"type":"Merge","objectId":"s89","x":0.16727697471797412,"y":0.5296379334815057,"nextId":"s90","prevId":"s88","alternativePrevId":"s108"},{"type":"Standart","objectId":"s90","x":0.1705072953517802,"y":0.6411701568461194,"nextId":"s91","prevId":"s89"},{"type":"TrafficLight","objectId":"s91","x":0.1737376159855863,"y":0.7675213469514861,"nextId":"s92","prevId":"s90","greenStartTime":"20","greenDuration":"10","redDuration":"20"},{"type":"Merge","objectId":"s92","x":0.13885015314048071,"y":0.8018389541405979,"nextId":"s93","prevId":"s91","alternativePrevId":"s177"},{"type":"Standart","objectId":"s93","x":0.05227756015447789,"y":0.8002790629047293,"nextId":"s94","prevId":"s92"},{"type":"ExitPoint","objectId":"s94","x":0.0018845582671032735,"y":0.7987191716688603,"prevId":"s93"},{"type":"EnterPoint","objectId":"s95","x":0.157586012816556,"y":0.003954586993745992,"nextId":"s96","minTime":"3","maxTime":"10"},{"type":"Standart","objectId":"s96","x":0.16598484646445172,"y":0.07648952946164159,"nextId":"s97","prevId":"s95"},{"type":"Fork","objectId":"s97","x":0.16663091059121293,"y":0.15292420001920912,"nextId":"s140","prevId":"s96","alternativeNextId":"s98","newPathProbability":"50"},{"type":"Merge","objectId":"s98","x":0.16986123122501903,"y":0.1841220247365835,"nextId":"s99","prevId":"s97","alternativePrevId":"s160"},{"type":"Standart","objectId":"s99","x":0.1717994236053028,"y":0.24963745664306997,"nextId":"s100","prevId":"s98"},{"type":"TrafficLight","objectId":"s100","x":0.17373761598558635,"y":0.4495235892163321,"nextId":"s101","prevId":"s99","greenStartTime":"10","greenDuration":"10","redDuration":"20"},{"type":"Fork","objectId":"s101","x":0.17438368011234748,"y":0.4680222296646912,"nextId":"s102","prevId":"s100","alternativeNextId":"s74","newPathProbability":"75"},{"type":"Merge","objectId":"s102","x":0.17826006487291485,"y":0.528078042245637,"nextId":"s103","prevId":"s101","alternativePrevId":"s112"},{"type":"Standart","objectId":"s103","x":0.18084432137995968,"y":0.6411701568461194,"nextId":"s104","prevId":"s102"},{"type":"TrafficLight","objectId":"s104","x":0.184720706140527,"y":0.7620617276259455,"nextId":"s105","prevId":"s103","greenStartTime":"10","greenDuration":"10","redDuration":"20"},{"type":"Fork","objectId":"s105","x":0.18601283439404945,"y":0.7800004768384358,"nextId":"s67","prevId":"s104","alternativeNextId":"s43","newPathProbability":"75"},{"type":"EnterPoint","objectId":"s106","x":0.0012384941403418219,"y":0.48362114202337836,"nextId":"s107","minTime":"10","maxTime":"20"},{"type":"Standart","objectId":"s107","x":0.03160350809811898,"y":0.48908076134891887,"nextId":"s108","prevId":"s106"},{"type":"TrafficLight","objectId":"s108","x":0.13626589663343583,"y":0.48752087011305023,"nextId":"s89","prevId":"s107","greenStartTime":"20","greenDuration":"10","redDuration":"20"},{"type":"Merge","objectId":"s109","x":0.4508991263661477,"y":0.4945403806744595,"nextId":"s110","prevId":"s50","alternativePrevId":"s182"},{"type":"Fork","objectId":"s110","x":0.3081189543519191,"y":0.497660163146197,"nextId":"s113","prevId":"s109","alternativeNextId":"s111","newPathProbability":"30"},{"type":"TrafficLight","objectId":"s111","x":0.20387230930955743,"y":0.497660163146197,"nextId":"s112","prevId":"s110","greenStartTime":"0","greenDuration":"10","redDuration":"20"},{"type":"Fork","objectId":"s112","x":0.18213644963348224,"y":0.4992200543820657,"nextId":"s13","prevId":"s111","alternativeNextId":"s102","newPathProbability":"75"},{"type":"Standart","objectId":"s113","x":0.2874449022955602,"y":0.48674092449511586,"nextId":"s114","prevId":"s110"},{"type":"TrafficLight","objectId":"s114","x":0.18795102677433304,"y":0.48674092449511586,"nextId":"s28","prevId":"s113","greenStartTime":"20","greenDuration":"10","redDuration":"20"},{"type":"EnterPoint","objectId":"s115","x":0.7965434341833978,"y":0.003954586993745931,"nextId":"s116","minTime":"5","maxTime":"20"},{"type":"TrafficLight","objectId":"s116","x":0.7952513059298755,"y":0.1326456139529157,"nextId":"s117","prevId":"s115","greenStartTime":"10","greenDuration":"10","redDuration":"20"},{"type":"Fork","objectId":"s117","x":0.7958973700566365,"y":0.1544840912550779,"nextId":"s148","prevId":"s116","alternativeNextId":"s118","newPathProbability":"30"},{"type":"Merge","objectId":"s118","x":0.7946052418031142,"y":0.21687974068982666,"nextId":"s119","prevId":"s117","alternativePrevId":"s154"},{"type":"Standart","objectId":"s119","x":0.7933131135495918,"y":0.3034537042805409,"nextId":"s120","prevId":"s118"},{"type":"Standart","objectId":"s120","x":0.8049422678312935,"y":0.3549301150642087,"nextId":"s121","prevId":"s119"},{"type":"TrafficLight","objectId":"s121","x":0.8075265243383385,"y":0.4329246768576449,"nextId":"s123","prevId":"s120","greenStartTime":"10","greenDuration":"10","redDuration":"30"},{"type":"Standart","objectId":"s123","x":0.812695037352428,"y":0.5241783141559652,"nextId":"s124","prevId":"s121"},{"type":"Merge","objectId":"s124","x":0.8146332297327119,"y":0.5748747793216987,"nextId":"s125","prevId":"s123","alternativePrevId":"s136"},{"type":"Merge","objectId":"s125","x":0.8159253579862342,"y":0.6255712444874322,"nextId":"s126","prevId":"s124","alternativePrevId":"s139"},{"type":"TrafficLight","objectId":"s126","x":0.8172174862397565,"y":0.7402232503237833,"nextId":"s127","prevId":"s125","greenStartTime":"0","greenDuration":"20","redDuration":"20"},{"type":"Fork","objectId":"s127","x":0.8146332297327119,"y":0.7792205312205015,"nextId":"s231","prevId":"s126","alternativeNextId":"s128","newPathProbability":"40"},{"type":"Merge","objectId":"s128","x":0.8269084481411748,"y":0.8197777033530882,"nextId":"s129","prevId":"s127","alternativePrevId":"s270"},{"type":"Fork","objectId":"s129","x":0.8533970773383845,"y":0.8821733527878373,"nextId":"s132","prevId":"s128","alternativeNextId":"s130","newPathProbability":"50"},{"type":"Standart","objectId":"s130","x":0.8895766684370128,"y":0.9211706336845552,"nextId":"s131","prevId":"s129"},{"type":"ExitPoint","objectId":"s131","x":0.9457842474652385,"y":0.9929256305345165,"prevId":"s130"},{"type":"Standart","objectId":"s132","x":0.8747171935215048,"y":0.9274101986280301,"nextId":"s133","prevId":"s129"},{"type":"ExitPoint","objectId":"s133","x":0.9322169008032531,"y":0.9960454130062539,"prevId":"s132"},{"type":"EnterPoint","objectId":"s134","x":0.998761505859658,"y":0.4968802175282625,"nextId":"s135","minTime":"15","maxTime":"20"},{"type":"Standart","objectId":"s135","x":0.9296326442962081,"y":0.4984401087641313,"nextId":"s136","prevId":"s134"},{"type":"Standart","objectId":"s136","x":0.8533970773383845,"y":0.5327577159532432,"nextId":"s124","prevId":"s135"},{"type":"EnterPoint","objectId":"s137","x":0.9981154417328968,"y":0.5101392930331468,"nextId":"s138","minTime":"5","maxTime":"25"},{"type":"Standart","objectId":"s138","x":0.9257562595356408,"y":0.5101392930331468,"nextId":"s139","prevId":"s137"},{"type":"Standart","objectId":"s139","x":0.8430600513102052,"y":0.5608357581988802,"nextId":"s125","prevId":"s138"},{"type":"Standart","objectId":"s140","x":0.2021644375630799,"y":0.16930305799583076,"nextId":"s141","prevId":"s97"},{"type":"Standart","objectId":"s141","x":0.32297842926742704,"y":0.16462338428822457,"nextId":"s142","prevId":"s140"},{"type":"TrafficLight","objectId":"s142","x":0.432685715577401,"y":0.1638434386702901,"nextId":"s143","prevId":"s141","greenStartTime":"0","greenDuration":"10","redDuration":"30"},{"type":"Fork","objectId":"s143","x":0.4534833828731925,"y":0.16150360181648712,"nextId":"s144","prevId":"s142","alternativeNextId":"s55","newPathProbability":"50"},{"type":"Merge","objectId":"s144","x":0.5032303206338059,"y":0.1638434386702901,"nextId":"s145","prevId":"s143","alternativePrevId":"s260"},{"type":"Fork","objectId":"s145","x":0.6485947491550795,"y":0.1638434386702901,"nextId":"s161","prevId":"s144","alternativeNextId":"s146","newPathProbability":"40"},{"type":"TrafficLight","objectId":"s146","x":0.7448583040425005,"y":0.1661832755240933,"nextId":"s147","prevId":"s145","greenStartTime":"0","greenDuration":"10","redDuration":"20"},{"type":"Standart","objectId":"s147","x":0.7616559713382921,"y":0.16228354743442133,"nextId":"s148","prevId":"s146"},{"type":"Merge","objectId":"s148","x":0.8088186525918608,"y":0.1661832755240933,"nextId":"s149","prevId":"s147","alternativePrevId":"s117"},{"type":"ExitPoint","objectId":"s149","x":0.998761505859658,"y":0.16228354743442133,"prevId":"s148"},{"type":"EnterPoint","objectId":"s150","x":0.998761505859658,"y":0.17710251417517434,"nextId":"s151","minTime":"10","maxTime":"20"},{"type":"Standart","objectId":"s151","x":0.9167113617609839,"y":0.18022229664691175,"nextId":"s152","prevId":"s150"},{"type":"Fork","objectId":"s152","x":0.8792396424088333,"y":0.18490197035451794,"nextId":"s189","prevId":"s151","alternativeNextId":"s153","newPathProbability":"60"},{"type":"TrafficLight","objectId":"s153","x":0.8264478068735627,"y":0.1888016984441897,"nextId":"s154","prevId":"s152","greenStartTime":"0","greenDuration":"10","redDuration":"20"},{"type":"Fork","objectId":"s154","x":0.8042962037045324,"y":0.1919214809159272,"nextId":"s118","prevId":"s153","alternativeNextId":"s155","newPathProbability":"50"},{"type":"Merge","objectId":"s155","x":0.7539032018171576,"y":0.18802175282625538,"nextId":"s156","prevId":"s154","alternativePrevId":"s204"},{"type":"Fork","objectId":"s156","x":0.6143533504367352,"y":0.18802175282625538,"nextId":"s261","prevId":"s155","alternativeNextId":"s157","newPathProbability":"50"},{"type":"TrafficLight","objectId":"s157","x":0.5142134107887467,"y":0.18724180720832087,"nextId":"s158","prevId":"s156","greenStartTime":"0","greenDuration":"10","redDuration":"30"},{"type":"Fork","objectId":"s158","x":0.4948314869859102,"y":0.18724180720832093,"nextId":"s237","prevId":"s157","alternativeNextId":"s159","newPathProbability":"30"},{"type":"Merge","objectId":"s159","x":0.44120816446472944,"y":0.18178218788278053,"nextId":"s160","prevId":"s158","alternativePrevId":"s54"},{"type":"Standart","objectId":"s160","x":0.2835685175349929,"y":0.18100224226484615,"nextId":"s98","prevId":"s159"},{"type":"Standart","objectId":"s161","x":0.6699148653381997,"y":0.17710251417517425,"nextId":"s162","prevId":"s145"},{"type":"TrafficLight","objectId":"s162","x":0.7506728811833515,"y":0.17710251417517425,"nextId":"s163","prevId":"s161","greenStartTime":"20","greenDuration":"10","redDuration":"20"},{"type":"Standart","objectId":"s163","x":0.785560344028457,"y":0.21142012136428626,"nextId":"s164","prevId":"s162"},{"type":"Standart","objectId":"s164","x":0.7868524722819797,"y":0.27381577079903513,"nextId":"s165","prevId":"s163"},{"type":"Fork","objectId":"s165","x":0.7862064081552184,"y":0.33153174652617784,"nextId":"s178","prevId":"s164","alternativeNextId":"s166","newPathProbability":"50"},{"type":"Standart","objectId":"s166","x":0.7965434341833978,"y":0.3650694080973554,"nextId":"s167","prevId":"s165"},{"type":"TrafficLight","objectId":"s167","x":0.797189498310159,"y":0.43370462247557917,"nextId":"s168","prevId":"s166","greenStartTime":"10","greenDuration":"10","redDuration":"30"},{"type":"Merge","objectId":"s168","x":0.8036501395777711,"y":0.5187186948304247,"nextId":"s169","prevId":"s167","alternativePrevId":"s206"},{"type":"Standart","objectId":"s169","x":0.8055883319580547,"y":0.64662977617166,"nextId":"s170","prevId":"s168"},{"type":"TrafficLight","objectId":"s170","x":0.8088186525918609,"y":0.7410031959417178,"nextId":"s171","prevId":"s169","greenStartTime":"0","greenDuration":"20","redDuration":"20"},{"type":"Standart","objectId":"s171","x":0.8042962037045323,"y":0.7776606399846328,"nextId":"s172","prevId":"s170"},{"type":"Standart","objectId":"s172","x":0.7707008691129491,"y":0.8104183559378758,"nextId":"s173","prevId":"s171"},{"type":"Fork","objectId":"s173","x":0.6376116590001388,"y":0.8080785190840729,"nextId":"s235","prevId":"s172","alternativeNextId":"s174","newPathProbability":"50"},{"type":"TrafficLight","objectId":"s174","x":0.5096909619014182,"y":0.8049587366123354,"nextId":"s175","prevId":"s173","greenStartTime":"0","greenDuration":"10","redDuration":"30"},{"type":"Merge","objectId":"s175","x":0.4483148698591028,"y":0.8026188997585324,"nextId":"s176","prevId":"s174","alternativePrevId":"s252"},{"type":"Standart","objectId":"s176","x":0.3268548140279943,"y":0.8049587366123354,"nextId":"s177","prevId":"s175"},{"type":"TrafficLight","objectId":"s177","x":0.2202542331123938,"y":0.8072985734661385,"nextId":"s92","prevId":"s176","greenStartTime":"0","greenDuration":"10","redDuration":"20"},{"type":"TrafficLight","objectId":"s178","x":0.7907288570425468,"y":0.435264513711448,"nextId":"s179","prevId":"s165","greenStartTime":"30","greenDuration":"10","redDuration":"30"},{"type":"Merge","objectId":"s179","x":0.7603638430847698,"y":0.49220054382065637,"nextId":"s180","prevId":"s178","alternativePrevId":"s194"},{"type":"Fork","objectId":"s180","x":0.6091848374226454,"y":0.49610027191032824,"nextId":"s265","prevId":"s179","alternativeNextId":"s181","newPathProbability":"50"},{"type":"TrafficLight","objectId":"s181","x":0.5136291542817019,"y":0.49610027191032824,"nextId":"s182","prevId":"s180","greenStartTime":"0","greenDuration":"10","redDuration":"20"},{"type":"Fork","objectId":"s182","x":0.49547755111267155,"y":0.4953203262923938,"nextId":"s183","prevId":"s181","alternativeNextId":"s109","newPathProbability":"50"},{"type":"Merge","objectId":"s183","x":0.48901690984505936,"y":0.5179387492124903,"nextId":"s184","prevId":"s182","alternativePrevId":"s240"},{"type":"Standart","objectId":"s184","x":0.48966297397182057,"y":0.6435099936999226,"nextId":"s185","prevId":"s183"},{"type":"TrafficLight","objectId":"s185","x":0.49030903809858173,"y":0.7449029240313896,"nextId":"s186","prevId":"s184","greenStartTime":"10","greenDuration":"10","redDuration":"30"},{"type":"Standart","objectId":"s186","x":0.49160116635210427,"y":0.7628416732438799,"nextId":"s187","prevId":"s185"},{"type":"Merge","objectId":"s187","x":0.49224723047886537,"y":0.832256833240038,"nextId":"s188","prevId":"s186","alternativePrevId":"s234"},{"type":"ExitPoint","objectId":"s188","x":0.49224723047886537,"y":0.9968253586241884,"prevId":"s187"},{"type":"Standart","objectId":"s189","x":0.8579195262257132,"y":0.17554262293930561,"nextId":"s190","prevId":"s152"},{"type":"TrafficLight","objectId":"s190","x":0.8114029090989057,"y":0.17710251417517425,"nextId":"s191","prevId":"s189","greenStartTime":"20","greenDuration":"10","redDuration":"20"},{"type":"Merge","objectId":"s191","x":0.7790997027608451,"y":0.14356485260399673,"nextId":"s192","prevId":"s190","alternativePrevId":"s204"},{"type":"ExitPoint","objectId":"s192","x":0.7862064081552184,"y":0.003954586993746053,"prevId":"s191"},{"type":"EnterPoint","objectId":"s193","x":0.9987615058596582,"y":0.48596097887718154,"nextId":"s194","minTime":"5","maxTime":"15"},{"type":"TrafficLight","objectId":"s194","x":0.8353072817890708,"y":0.49220054382065637,"nextId":"s179","prevId":"s193","greenStartTime":"0","greenDuration":"10","redDuration":"30"},{"type":"Standart","objectId":"s195","x":0.5077527695211346,"y":0.7745408575128954,"nextId":"s196","prevId":"s46"},{"type":"Standart","objectId":"s196","x":0.620167927577586,"y":0.7768806943666983,"nextId":"s197","prevId":"s195"},{"type":"TrafficLight","objectId":"s197","x":0.7558413941974412,"y":0.7776606399846328,"nextId":"s198","prevId":"s196","greenStartTime":"0","greenDuration":"20","redDuration":"20"},{"type":"Standart","objectId":"s198","x":0.7771615103805612,"y":0.7519224345927988,"nextId":"s199","prevId":"s197"},{"type":"Standart","objectId":"s199","x":0.7803918310143674,"y":0.6286910269591698,"nextId":"s200","prevId":"s198"},{"type":"TrafficLight","objectId":"s200","x":0.779099702760845,"y":0.519498640448359,"nextId":"s201","prevId":"s199","greenStartTime":"10","greenDuration":"10","redDuration":"30"},{"type":"Merge","objectId":"s201","x":0.7797457668876062,"y":0.44228402427285735,"nextId":"s202","prevId":"s200","alternativePrevId":"s82"},{"type":"Standart","objectId":"s202","x":0.7758693821270389,"y":0.31203310607781887,"nextId":"s203","prevId":"s201"},{"type":"TrafficLight","objectId":"s203","x":0.7758693821270388,"y":0.2212195775436298,"nextId":"s204","prevId":"s202","greenStartTime":"10","greenDuration":"10","redDuration":"20"},{"type":"Fork","objectId":"s204","x":0.7745772538735165,"y":0.20284071956700836,"nextId":"s155","prevId":"s203","alternativeNextId":"s191","newPathProbability":"25"},{"type":"Standart","objectId":"s205","x":0.6718530577184832,"y":0.4828411964054441,"nextId":"s206","prevId":"s80"},{"type":"TrafficLight","objectId":"s206","x":0.7597177789580086,"y":0.4805013595516409,"nextId":"s168","prevId":"s205","greenStartTime":"20","greenDuration":"10","redDuration":"30"},{"type":"Standart","objectId":"s207","x":0.3785399441688916,"y":0.7862400417819106,"nextId":"s208","prevId":"s36"},{"type":"TrafficLight","objectId":"s208","x":0.44960699811262517,"y":0.7862400417819106,"nextId":"s209","prevId":"s207","greenStartTime":"0","greenDuration":"10","redDuration":"30"},{"type":"Merge","objectId":"s209","x":0.505814577140851,"y":0.7877999330177794,"nextId":"s210","prevId":"s208","alternativePrevId":"s255"},{"type":"Fork","objectId":"s210","x":0.6195218634508248,"y":0.7885798786357138,"nextId":"s269","prevId":"s209","alternativeNextId":"s211","newPathProbability":"50"},{"type":"TrafficLight","objectId":"s211","x":0.7539032018171576,"y":0.7916996611074512,"nextId":"s212","prevId":"s210","greenStartTime":"0","greenDuration":"20","redDuration":"20"},{"type":"Merge","objectId":"s212","x":0.7900827929157856,"y":0.758941945154208,"nextId":"s213","prevId":"s211","alternativePrevId":"s226"},{"type":"Standart","objectId":"s213","x":0.7887906646622631,"y":0.629470972577104,"nextId":"s214","prevId":"s212"},{"type":"TrafficLight","objectId":"s214","x":0.788144600535502,"y":0.519498640448359,"nextId":"s83","prevId":"s213","greenStartTime":"30","greenDuration":"10","redDuration":"30"},{"type":"EnterPoint","objectId":"s215","x":0.8934530531975801,"y":0.9952654673883198,"nextId":"s216","minTime":"5","maxTime":"15"},{"type":"Standart","objectId":"s216","x":0.8501667567045786,"y":0.9133711775052116,"nextId":"s217","prevId":"s215"},{"type":"TrafficLight","objectId":"s217","x":0.8191556786200402,"y":0.8338167244759066,"nextId":"s218","prevId":"s216","greenStartTime":"30","greenDuration":"10","redDuration":"30"},{"type":"Standart","objectId":"s218","x":0.7997737548172039,"y":0.7503625433569301,"nextId":"s219","prevId":"s217"},{"type":"Standart","objectId":"s219","x":0.7965434341833978,"y":0.6279110813412353,"nextId":"s220","prevId":"s218"},{"type":"TrafficLight","objectId":"s220","x":0.7958973700566366,"y":0.5179387492124903,"nextId":"s221","prevId":"s219","greenStartTime":"30","greenDuration":"10","redDuration":"30"},{"type":"Standart","objectId":"s221","x":0.8373241916341301,"y":0.4766016314619691,"nextId":"s222","prevId":"s220"},{"type":"Standart","objectId":"s222","x":0.89345305319758,"y":0.4727019033722974,"nextId":"s223","prevId":"s221"},{"type":"ExitPoint","objectId":"s223","x":0.9994075699864196,"y":0.4727019033722974,"prevId":"s222"},{"type":"EnterPoint","objectId":"s224","x":0.8837620912961618,"y":0.9999451410959259,"nextId":"s225","minTime":"10","maxTime":"20"},{"type":"Standart","objectId":"s225","x":0.8508128208313399,"y":0.9422291653687831,"nextId":"s226","prevId":"s224"},{"type":"TrafficLight","objectId":"s226","x":0.8094647167186221,"y":0.8377164525655786,"nextId":"s212","prevId":"s225","greenStartTime":"30","greenDuration":"10","redDuration":"30"},{"type":"EnterPoint","objectId":"s227","x":0.8676104881271315,"y":0.9976053042421228,"nextId":"s228","minTime":"15","maxTime":"25"},{"type":"Standart","objectId":"s228","x":0.8404757948031604,"y":0.9406692741329142,"nextId":"s229","prevId":"s227"},{"type":"Standart","objectId":"s229","x":0.8139871656059507,"y":0.8868530264954434,"nextId":"s230","prevId":"s228"},{"type":"TrafficLight","objectId":"s230","x":0.7952513059298754,"y":0.8369365069476441,"nextId":"s231","prevId":"s229","greenStartTime":"20","greenDuration":"10","redDuration":"30"},{"type":"Merge","objectId":"s231","x":0.7752233180002777,"y":0.8205576489710226,"nextId":"s232","prevId":"s230","alternativePrevId":"s127"},{"type":"Standart","objectId":"s232","x":0.6382577231269001,"y":0.8174378664992852,"nextId":"s233","prevId":"s231"},{"type":"TrafficLight","objectId":"s233","x":0.5207976672957915,"y":0.8166579208813507,"nextId":"s234","prevId":"s232","greenStartTime":"0","greenDuration":"10","redDuration":"30"},{"type":"Fork","objectId":"s234","x":0.49635393587323884,"y":0.8150980296454821,"nextId":"s187","prevId":"s233","alternativeNextId":"s62","newPathProbability":"50"},{"type":"Standart","objectId":"s235","x":0.6059545167888393,"y":0.7971592804329917,"nextId":"s236","prevId":"s173"},{"type":"TrafficLight","objectId":"s236","x":0.5077527695211346,"y":0.795599389197123,"nextId":"s47","prevId":"s235","greenStartTime":"20","greenDuration":"10","redDuration":"30"},{"type":"Merge","objectId":"s237","x":0.4883708457182982,"y":0.2137599582180893,"nextId":"s238","prevId":"s158","alternativePrevId":"s243"},{"type":"Standart","objectId":"s238","x":0.49030903809858173,"y":0.34245098517725897,"nextId":"s239","prevId":"s237"},{"type":"TrafficLight","objectId":"s239","x":0.49030903809858173,"y":0.4500834804522008,"nextId":"s240","prevId":"s238","greenStartTime":"10","greenDuration":"10","redDuration":"20"},{"type":"Fork","objectId":"s240","x":0.490955102225343,"y":0.46646233842882245,"nextId":"s183","prevId":"s239","alternativeNextId":"s79","newPathProbability":"70"},{"type":"EnterPoint","objectId":"s241","x":0.4883708457182982,"y":0.001614750139943014,"nextId":"s242","minTime":"5","maxTime":"15"},{"type":"Standart","objectId":"s242","x":0.48772478159153687,"y":0.06167056272088873,"nextId":"s243","prevId":"s241"},{"type":"TrafficLight","objectId":"s243","x":0.48966297397182057,"y":0.13576539642465316,"nextId":"s237","prevId":"s242","greenStartTime":"10","greenDuration":"10","redDuration":"30"},{"type":"EnterPoint","objectId":"s244","x":0.47997201207040235,"y":0.0023946957578772185,"nextId":"s245","minTime":"5","maxTime":"15"},{"type":"Standart","objectId":"s245","x":0.4793259479436411,"y":0.062450508338823235,"nextId":"s246","prevId":"s244"},{"type":"TrafficLight","objectId":"s246","x":0.48126414032392484,"y":0.1326456139529157,"nextId":"s247","prevId":"s245","greenStartTime":"10","greenDuration":"10","redDuration":"30"},{"type":"Standart","objectId":"s247","x":0.4806180761971635,"y":0.215319849453958,"nextId":"s248","prevId":"s246"},{"type":"Standart","objectId":"s248","x":0.48126414032392484,"y":0.33699136585171846,"nextId":"s249","prevId":"s247"},{"type":"TrafficLight","objectId":"s249","x":0.481910204450686,"y":0.4516433716880696,"nextId":"s250","prevId":"s248","greenStartTime":"10","greenDuration":"10","redDuration":"20"},{"type":"Merge","objectId":"s250","x":0.48126414032392484,"y":0.5218384773021622,"nextId":"s251","prevId":"s249","alternativePrevId":"s268"},{"type":"Standart","objectId":"s251","x":0.481910204450686,"y":0.6450698849357912,"nextId":"s252","prevId":"s250"},{"type":"TrafficLight","objectId":"s252","x":0.48255626857744727,"y":0.7597218907721424,"nextId":"s175","prevId":"s251","greenStartTime":"30","greenDuration":"10","redDuration":"30"},{"type":"EnterPoint","objectId":"s253","x":0.47480349905631264,"y":0.9976053042421227,"nextId":"s254","minTime":"5","maxTime":"25"},{"type":"Standart","objectId":"s254","x":0.472865306676029,"y":0.9219505793024896,"nextId":"s255","prevId":"s253"},{"type":"TrafficLight","objectId":"s255","x":0.47415743492955137,"y":0.8338167244759067,"nextId":"s209","prevId":"s254","greenStartTime":"30","greenDuration":"10","redDuration":"30"},{"type":"Standart","objectId":"s256","x":0.47480349905631264,"y":0.6240113532515635,"nextId":"s257","prevId":"s48"},{"type":"TrafficLight","objectId":"s257","x":0.47351137080279015,"y":0.5226184229200965,"nextId":"s258","prevId":"s256","greenStartTime":"10","greenDuration":"10","redDuration":"20"},{"type":"Merge","objectId":"s258","x":0.4754495631830738,"y":0.45788293663154445,"nextId":"s259","prevId":"s257","alternativePrevId":"s266"},{"type":"Standart","objectId":"s259","x":0.47415743492955137,"y":0.30891332360608137,"nextId":"s260","prevId":"s258"},{"type":"TrafficLight","objectId":"s260","x":0.47351137080279015,"y":0.21220006698222058,"nextId":"s144","prevId":"s259","greenStartTime":"30","greenDuration":"10","redDuration":"30"},{"type":"Standart","objectId":"s261","x":0.5936792983803763,"y":0.17554262293930556,"nextId":"s262","prevId":"s156"},{"type":"TrafficLight","objectId":"s262","x":0.5045224488873286,"y":0.17476267732137124,"nextId":"s263","prevId":"s261","greenStartTime":"20","greenDuration":"10","redDuration":"30"},{"type":"Standart","objectId":"s263","x":0.4722192425492677,"y":0.1365453420425875,"nextId":"s264","prevId":"s262"},{"type":"ExitPoint","objectId":"s264","x":0.47092711429574535,"y":0.0023946957578772792,"prevId":"s263"},{"type":"Standart","objectId":"s265","x":0.5917411060000926,"y":0.4844010876413128,"nextId":"s266","prevId":"s180"},{"type":"TrafficLight","objectId":"s266","x":0.5025842565070447,"y":0.4828411964054441,"nextId":"s258","prevId":"s265","greenStartTime":"20","greenDuration":"10","redDuration":"20"},{"type":"Standart","objectId":"s267","x":0.3946915473379219,"y":0.4844010876413128,"nextId":"s268","prevId":"s76"},{"type":"TrafficLight","objectId":"s268","x":0.4502530622393865,"y":0.48518103325924716,"nextId":"s250","prevId":"s267","greenStartTime":"20","greenDuration":"10","redDuration":"20"},{"type":"Standart","objectId":"s269","x":0.6569935828029753,"y":0.7987191716688603,"nextId":"s270","prevId":"s210"},{"type":"TrafficLight","objectId":"s270","x":0.7539032018171576,"y":0.8010590085226637,"nextId":"s128","prevId":"s269","greenStartTime":"20","greenDuration":"10","redDuration":"30"}]', 6);