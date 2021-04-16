package uz.pixyz.rentcar.models

class Location {
    var locName: String? = null
    var locDesc: String? = null
    var locImage: Int? = null

    constructor(locName: String?, locDesc: String?) {
        this.locName = locName
        this.locDesc = locDesc
    }

}