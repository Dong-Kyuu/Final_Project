drop table TripFile;

CREATE TABLE TripFile (
                          fileId        VARCHAR(10) NOT NULL PRIMARY KEY, -- 상품번호와 동일하게
                          mainIMG       VARCHAR(60),
                          introIMG      VARCHAR(60),
                          routeIMG      VARCHAR(60),
                          scheduleIMG   VARCHAR(60),
                          detailIMG     VARCHAR(60)
);


delete from tripfile;

select * from TripFile;

insert into TripFile (fileId, mainIMG, introIMG, routeIMG, scheduleIMG, detailIMG)
values('1', '/image/product/mainIMG/WEU1.png', '/image/product/introIMG/Wintro1.png', '/image/product/routeIMG/WEUroute.png', '/image/product/scheduleIMG/WEUschedule.png', '/image/product/detailIMG/Wdetail1.png');

insert into TripFile (fileId, mainIMG, introIMG, routeIMG, scheduleIMG, detailIMG)
values('2', '/image/product/mainIMG/WEU2.png', '/image/product/introIMG/Wintro2.png', '/image/product/routeIMG/WEUroute.png', '/image/product/scheduleIMG/WEUschedule.png', '/image/product/detailIMG/Wdetail2.png');

insert into TripFile (fileId, mainIMG, introIMG, routeIMG, scheduleIMG, detailIMG)
values('3', '/image/product/mainIMG/WEU3.png', '/image/product/introIMG/Wintro3.png', '/image/product/routeIMG/WEUroute.png', '/image/product/scheduleIMG/WEUschedule.png', '/image/product/detailIMG/Wdetail3.png');

insert into TripFile (fileId, mainIMG, introIMG, routeIMG, scheduleIMG, detailIMG)
values('4', '/image/product/mainIMG/WEU4.png', '/image/product/introIMG/Wintro4.png', '/image/product/routeIMG/WEUroute.png', '/image/product/scheduleIMG/WEUschedule.png', '/image/product/detailIMG/Wdetail4.png');

insert into TripFile (fileId, mainIMG, introIMG, routeIMG, scheduleIMG, detailIMG)
values('5', '/image/product/mainIMG/WEU5.png', '/image/product/introIMG/Wintro5.png', '/image/product/routeIMG/WEUroute.png', '/image/product/scheduleIMG/WEUschedule.png', '/image/product/detailIMG/Wdetail5.png');

insert into TripFile (fileId, mainIMG, introIMG, routeIMG, scheduleIMG, detailIMG)
values('6', '/image/product/mainIMG/WEU6.png', '/image/product/introIMG/Wintro6.png', '/image/product/routeIMG/WEUroute.png', '/image/product/scheduleIMG/WEUschedule.png', '/image/product/detailIMG/Wdetail6.png');

insert into TripFile (fileId, mainIMG, introIMG, routeIMG, scheduleIMG, detailIMG)
values('7', '/image/product/mainIMG/WEU7.png', '/image/product/introIMG/Wintro7.png', '/image/product/routeIMG/WEUroute.png', '/image/product/scheduleIMG/WEUschedule.png', '/image/product/detailIMG/Wdetail7.png');

insert into TripFile (fileId, mainIMG, introIMG, routeIMG, scheduleIMG, detailIMG)
values('8', '/image/product/mainIMG/WEU8.png', '/image/product/introIMG/Wintro8.png', '/image/product/routeIMG/WEUroute.png', '/image/product/scheduleIMG/WEUschedule.png', '/image/product/detailIMG/Wdetail8.png');

insert into TripFile (fileId, mainIMG, introIMG, routeIMG, scheduleIMG, detailIMG)
values('9', '/image/product/mainIMG/CEU1.png', '/image/product/introIMG/CEUIntro.png', '/image/product/routeIMG/CEUroute.png', '/image/product/scheduleIMG/CEUschedule.png', '/image/product/detailIMG/CEUdetail1.png');

insert into TripFile (fileId, mainIMG, introIMG, routeIMG, scheduleIMG, detailIMG)
values('10', '/image/product/mainIMG/CEU2.png', '/image/product/introIMG/CEUIntro.png', '/image/product/routeIMG/CEUroute.png', '/image/product/scheduleIMG/CEUschedule.png', '/image/product/detailIMG/CEUdetail2.png');

insert into TripFile (fileId, mainIMG, introIMG, routeIMG, scheduleIMG, detailIMG)
values('11', '/image/product/mainIMG/CEU3.png', '/image/product/introIMG/CEUIntro.png', '/image/product/routeIMG/CEUroute.png', '/image/product/scheduleIMG/CEUschedule.png', '/image/product/detailIMG/CEUdetail3.png');

insert into TripFile (fileId, mainIMG, introIMG, routeIMG, scheduleIMG, detailIMG)
values('12', '/image/product/mainIMG/CEU4.png', '/image/product/introIMG/CEUIntro.png', '/image/product/routeIMG/CEUroute.png', '/image/product/scheduleIMG/CEUschedule.png', '/image/product/detailIMG/CEUdetail4.png');

insert into TripFile (fileId, mainIMG, introIMG, routeIMG, scheduleIMG, detailIMG)
values('13', '/image/product/mainIMG/CEU5.png', '/image/product/introIMG/CEUIntro.png', '/image/product/routeIMG/CEUroute.png', '/image/product/scheduleIMG/CEUschedule.png', '/image/product/detailIMG/CEUdetail5.png');

insert into TripFile (fileId, mainIMG, introIMG, routeIMG, scheduleIMG, detailIMG)
values('14', '/image/product/mainIMG/SEU1.png', '/image/product/introIMG/SEUIntro.png', '/image/product/routeIMG/SEUroute.png', '/image/product/scheduleIMG/SEUschedule.png', '/image/product/detailIMG/SEUdetail1.png');

insert into TripFile (fileId, mainIMG, introIMG, routeIMG, scheduleIMG, detailIMG)
values('15', '/image/product/mainIMG/SEU2.png', '/image/product/introIMG/SEUIntro.png', '/image/product/routeIMG/SEUroute.png', '/image/product/scheduleIMG/SEUschedule.png', '/image/product/detailIMG/SEUdetail2.png');

insert into TripFile (fileId, mainIMG, introIMG, routeIMG, scheduleIMG, detailIMG)
values('16', '/image/product/mainIMG/SEU3.png', '/image/product/introIMG/SEUIntro.png', '/image/product/routeIMG/SEUroute.png', '/image/product/scheduleIMG/SEUschedule.png', '/image/product/detailIMG/SEUdetail3.png');

insert into TripFile (fileId, mainIMG, introIMG, routeIMG, scheduleIMG, detailIMG)
values('17', '/image/product/mainIMG/SEU4.png', '/image/product/introIMG/SEUIntro.png', '/image/product/routeIMG/SEUroute.png', '/image/product/scheduleIMG/SEUschedule.png', '/image/product/detailIMG/SEUdetail4.png');

insert into TripFile (fileId, mainIMG, introIMG, routeIMG, scheduleIMG, detailIMG)
values('18', '/image/product/mainIMG/SEU5.png', '/image/product/introIMG/SEUIntro.png', '/image/product/routeIMG/SEUroute.png', '/image/product/scheduleIMG/SEUschedule.png', '/image/product/detailIMG/SEUdetail5.png');

insert into TripFile (fileId, mainIMG, introIMG, routeIMG, scheduleIMG, detailIMG)
values('19', '/image/product/mainIMG/SEU6.png', '/image/product/introIMG/SEUIntro.png', '/image/product/routeIMG/SEUroute.png', '/image/product/scheduleIMG/SEUschedule.png', '/image/product/detailIMG/SEUdetail6.png');



-- -----------------------------

DROP TABLE IF EXISTS tripfile;

CREATE TABLE tripfile (
                          file_id        VARCHAR(10) NOT NULL PRIMARY KEY, -- 상품번호와 동일하게
                          main_img       VARCHAR(60),
                          intro_img      VARCHAR(60),
                          route_img      VARCHAR(60),
                          schedule_img   VARCHAR(60),
                          detail_img     VARCHAR(60)
);

DELETE FROM tripfile;

SELECT * FROM tripfile;

INSERT INTO tripfile (file_id, main_img, intro_img, route_img, schedule_img, detail_img)
VALUES
    ('1', '/image/product/mainIMG/WEU1.png', '/image/product/introIMG/Wintro1.png', '/image/product/routeIMG/WEUroute.png', '/image/product/scheduleIMG/WEUschedule.png', '/image/product/detailIMG/Wdetail1.png'),
    ('2', '/image/product/mainIMG/WEU2.png', '/image/product/introIMG/Wintro2.png', '/image/product/routeIMG/WEUroute.png', '/image/product/scheduleIMG/WEUschedule.png', '/image/product/detailIMG/Wdetail2.png'),
    ('3', '/image/product/mainIMG/WEU3.png', '/image/product/introIMG/Wintro3.png', '/image/product/routeIMG/WEUroute.png', '/image/product/scheduleIMG/WEUschedule.png', '/image/product/detailIMG/Wdetail3.png'),
    ('4', '/image/product/mainIMG/WEU4.png', '/image/product/introIMG/Wintro4.png', '/image/product/routeIMG/WEUroute.png', '/image/product/scheduleIMG/WEUschedule.png', '/image/product/detailIMG/Wdetail4.png'),
    ('5', '/image/product/mainIMG/WEU5.png', '/image/product/introIMG/Wintro5.png', '/image/product/routeIMG/WEUroute.png', '/image/product/scheduleIMG/WEUschedule.png', '/image/product/detailIMG/Wdetail5.png'),
    ('6', '/image/product/mainIMG/WEU6.png', '/image/product/introIMG/Wintro6.png', '/image/product/routeIMG/WEUroute.png', '/image/product/scheduleIMG/WEUschedule.png', '/image/product/detailIMG/Wdetail6.png'),
    ('7', '/image/product/mainIMG/WEU7.png', '/image/product/introIMG/Wintro7.png', '/image/product/routeIMG/WEUroute.png', '/image/product/scheduleIMG/WEUschedule.png', '/image/product/detailIMG/Wdetail7.png'),
    ('8', '/image/product/mainIMG/WEU8.png', '/image/product/introIMG/Wintro8.png', '/image/product/routeIMG/WEUroute.png', '/image/product/scheduleIMG/WEUschedule.png', '/image/product/detailIMG/Wdetail8.png'),
    ('9', '/image/product/mainIMG/CEU1.png', '/image/product/introIMG/CEUIntro.png', '/image/product/routeIMG/CEUroute.png', '/image/product/scheduleIMG/CEUschedule.png', '/image/product/detailIMG/CEUdetail1.png'),
    ('10', '/image/product/mainIMG/CEU2.png', '/image/product/introIMG/CEUIntro.png', '/image/product/routeIMG/CEUroute.png', '/image/product/scheduleIMG/CEUschedule.png', '/image/product/detailIMG/CEUdetail2.png'),
    ('11', '/image/product/mainIMG/CEU3.png', '/image/product/introIMG/CEUIntro.png', '/image/product/routeIMG/CEUroute.png', '/image/product/scheduleIMG/CEUschedule.png', '/image/product/detailIMG/CEUdetail3.png'),
    ('12', '/image/product/mainIMG/CEU4.png', '/image/product/introIMG/CEUIntro.png', '/image/product/routeIMG/CEUroute.png', '/image/product/scheduleIMG/CEUschedule.png', '/image/product/detailIMG/CEUdetail4.png'),
    ('13', '/image/product/mainIMG/CEU5.png', '/image/product/introIMG/CEUIntro.png', '/image/product/routeIMG/CEUroute.png', '/image/product/scheduleIMG/CEUschedule.png', '/image/product/detailIMG/CEUdetail5.png'),
    ('14', '/image/product/mainIMG/SEU1.png', '/image/product/introIMG/SEUIntro.png', '/image/product/routeIMG/SEUroute.png', '/image/product/scheduleIMG/SEUschedule.png', '/image/product/detailIMG/SEUdetail1.png'),
    ('15', '/image/product/mainIMG/SEU2.png', '/image/product/introIMG/SEUIntro.png', '/image/product/routeIMG/SEUroute.png', '/image/product/scheduleIMG/SEUschedule.png', '/image/product/detailIMG/SEUdetail2.png'),
    ('16', '/image/product/mainIMG/SEU3.png', '/image/product/introIMG/SEUIntro.png', '/image/product/routeIMG/SEUroute.png', '/image/product/scheduleIMG/SEUschedule.png', '/image/product/detailIMG/SEUdetail3.png'),
    ('17', '/image/product/mainIMG/SEU4.png', '/image/product/introIMG/SEUIntro.png', '/image/product/routeIMG/SEUroute.png', '/image/product/scheduleIMG/SEUschedule.png', '/image/product/detailIMG/SEUdetail4.png'),
    ('18', '/image/product/mainIMG/SEU5.png', '/image/product/introIMG/SEUIntro.png', '/image/product/routeIMG/SEUroute.png', '/image/product/scheduleIMG/SEUschedule.png', '/image/product/detailIMG/SEUdetail5.png'),
    ('19', '/image/product/mainIMG/SEU6.png', '/image/product/introIMG/SEUIntro.png', '/image/product/routeIMG/SEUroute.png', '/image/product/scheduleIMG/SEUschedule.png', '/image/product/detailIMG/SEUdetail6.png')



