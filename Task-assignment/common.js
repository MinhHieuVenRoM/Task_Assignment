const _MS_PER_DAY = 1000 * 60 * 60 * 24

module.exports = {

    getDateFromGMT: function(date){
        var temp = new Date(date)
        console.log(date)
        var gmtTime = temp.toLocaleString("en-US", {timeZone: "Europe/London"});
        var newDate = new Date(gmtTime)
        return newDate
    },

    // a and b are javascript Date objects
    dateDiffInDays: function(a, b) {
        
    // Discard the time and time-zone information.
        const utc1 = Date.UTC(a.getFullYear(), a.getMonth(), a.getDate())
        const utc2 = Date.UTC(b.getFullYear(), b.getMonth(), b.getDate())

        return Math.floor((utc2 - utc1) / _MS_PER_DAY);
    },

    getUnique: function(arr, comp) {

        const unique = arr
             .map(e => e[comp])
      
           // store the keys of the unique objects
          .map((e, i, final) => final.indexOf(e) === i && i)
      
          // eliminate the dead keys & store unique objects
          .filter(e => arr[e]).map(e => arr[e]);
      
         return unique;
      }
}
