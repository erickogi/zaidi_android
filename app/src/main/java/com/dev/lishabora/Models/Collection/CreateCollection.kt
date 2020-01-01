package com.dev.lishabora.Models.Collection

import com.dev.lishabora.Models.Collection
import com.dev.lishabora.Models.FamerModel
import com.dev.lishabora.Models.ResponseModel

class CreateCollection {
    private var collection: Collection? = null
    private var famerModel: FamerModel? = null
    private var responseModel: ResponseModel?=null

    constructor (collection: Collection, famerModel: FamerModel, responseModel: ResponseModel) {
        this.collection = collection
        this.famerModel = famerModel
        this.responseModel = responseModel
    }

    fun getResponseModel(): ResponseModel? {
        return responseModel
    }

    fun getCollection(): Collection? {
        return collection
    }

    fun setCollection(collection: Collection) {
        this.collection = collection
    }

    fun getFamerModel(): FamerModel? {
        return famerModel
    }

    fun setFamerModel(famerModel: FamerModel) {
        this.famerModel = famerModel
    }
}