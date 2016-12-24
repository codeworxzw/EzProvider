$(function(){
    //var hash = window.location.hash;
    //hash && $('ul.nav a[href="' + hash + '"]').tab('show');
    //console.log('loaaad', hash);
    //$('ul.nav a[href="' + hash + '"]').tab('show');
    //$('ul.nav a[href="' + hash + '"]').tab('show');
    //$('.nav-tabs a').click(function (e) {
    //    console.log('cliick',e);
    //    $(this).tab('show');
    //    var scrollmem = $('body').scrollTop() || $('html').scrollTop();
    //    window.location.hash = this.hash;
    //    $('html,body').scrollTop(scrollmem);
    //});
    //$(".nav-tabs a").click(function(){
    //    console.log('a');
    //    $(this).tab('show');
    //});
    //$('.nav-tabs a:last').tab('show');
});

function isObject(val) {
    return val instanceof Object;
}

function replaceAll(str, find, replace) {
    var i = str.indexOf(find);
    if (i > -1){
        str = str.replace(find, replace);
        i = i + replace.length;
        var st2 = str.substring(i);
        if(st2.indexOf(find) > -1){
            str = str.substring(0,i) + replaceAll(st2, find, replace);
        }
    }
    return str;
}

function map_load(lat, lng) {
    window.open('https://www.google.mn/maps/@'+lat+','+lng+',14z');
};

function dateDayLast() {
    var today = new Date();
    var dateIn = new Date(today.getFullYear(), today.getMonth()+1, 0);
    var yyyy = dateIn.getFullYear();
    var mm = dateIn.getMonth()+1; // getMonth() is zero-based
    var dd  = dateIn.getDate();
    return yyyy+'/'+(mm<10?'0'+mm:mm)+'/'+(dd<10?'0'+dd:dd);
}

function dateDay(dateIn) {
    var yyyy = dateIn.getFullYear();
    var mm = dateIn.getMonth()+1; // getMonth() is zero-based
    var dd  = dateIn.getDate();
    return yyyy+'/'+(mm<10?'0'+mm:mm)+'/'+(dd<10?'0'+dd:dd);
}

function dateStr(dateIn) {
    var yyyy = dateIn.getFullYear();
    var mm = dateIn.getMonth()+1; // getMonth() is zero-based
    var dd  = dateIn.getDate();
    var hh = dateIn.getHours();
    var m = dateIn.getMinutes();
    var ss = dateIn.getSeconds();
    return yyyy+'-'+(mm<10?'0'+mm:mm)+'-'+(dd<10?'0'+dd:dd)+' '+(hh<10?'0'+hh:hh)+':'+(m<10?'0'+m:m)+':'+(ss<10?'0'+ss:ss); // Leading zeros for mm and dd
}

function findAndRemove(array, property, value) {
    array.forEach(function(result, index) {
        if(result[property] === value) {
            //Remove from array
            array.splice(index, 1);
        }
    });
}