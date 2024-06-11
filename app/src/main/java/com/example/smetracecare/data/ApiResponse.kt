package com.example.smetracecare.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName

data class DataLogin(
	var email: String,
	var password: String,
	var role: String
)

data class LoginResponse(
	@field:SerializedName("error")
	var error: Boolean,

	@field:SerializedName("message")
	var message: String,

	@field:SerializedName("result")
	var result: LoginResult?
)

data class GetSupplierProfileResponse(
	@field:SerializedName("error")
	var error: Boolean,

	@field:SerializedName("message")
	var message: String,

	@field:SerializedName("result")
	var result: ProfileResult?
)

data class LoginResult(
	@field:SerializedName("userId")
	var userId: String,

	@field:SerializedName("name")
	var name: String,

	@field:SerializedName("email")
	var email: String,

	@field:SerializedName("description")
	var description: String,

	@field:SerializedName("address")
	var address: String,

	@field:SerializedName("phoneNumber")
	var phoneNumber: String,

	@field:SerializedName("token")
	var token: String
)

data class ProfileResult(
	@field:SerializedName("userId")
	var userId: String,

	@field:SerializedName("name")
	var name: String,

	@field:SerializedName("email")
	var email: String,

	@field:SerializedName("description")
	var description: String,

	@field:SerializedName("address")
	var address: String,

	@field:SerializedName("phoneNumber")
	var phoneNumber: String
)

data class ErrorResponse(
	@field:SerializedName("error")
	var error: Boolean,

	@field:SerializedName("message")
	var message: String
)
data class DataRegister(
	var name: String,
	var email: String,
	var password: String,
	var role: String,
	var description: String,
	var address: String,
	var phoneNumber: String,
	var confirmPassword: String
)

data class DetailResponse(
	var error: Boolean,
	var message: String
)

data class UpdateSupplierProfileRequest(
	val name: String,
	val email: String,
	val phoneNumber: String,
	val address: String,
	val description: String
)

data class UpdateSupplierProfileResponse(
	val success: Boolean,
	val message: String
)

data class GetMaterial(
	var error: String,
	var message: String,
	var result: List<MaterialDetail>
)

@Parcelize
data class MaterialDetail (
	var materialId: String,
	var name: String,
	var description: String,
	var image: String,
	var type: String,
	var price: String,
	var supplierId: String
) : Parcelable

data class DataAddMaterial(
	var name: String,
	var description: String,
	var image: String,
	var type: String,
	var price: String,
	var supplierId: String
)

data class ResponseAddMaterial(
	var error: String,
	var message: String,
	var result: MaterialDetail
)