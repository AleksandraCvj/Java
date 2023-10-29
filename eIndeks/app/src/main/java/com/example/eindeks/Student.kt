package com.example.eindeks

class Student {

    private var id: Long = 0
    private var name: String? = null
    private var surname: String? = null
    private var indeks: String? = null
    private var jmbg: String? = null
    private var username: String? = null
    private var password: String? = null

    fun Student() {}

    fun setId(id: Long) {
        this.id = id
    }

    fun getId(): Long {
        return id
    }

    fun setName(name: String){
        this.name = name
    }

    fun getName(): String? {
        return name
    }

    fun setSurname(surname: String?) {
        this.surname = surname
    }

    fun getSurname(): String?{
        return surname
    }

    fun setJmbg(jmbg: String?) {
        this.jmbg = jmbg
    }

    fun getJmbg(): String?{
        return jmbg
    }

    fun setIndeks(indeks: String?) {
        this.indeks = indeks
    }

    fun getIndeks(): String?{
        return indeks
    }

    fun setUsername(username: String?) {
        this.username = username
    }

    fun getUsername(): String?{
        return username
    }

    fun setPassword(password: String?) {
        this.password = password
    }

    fun getPassword(): String?{
        return password
    }







}