package mez.com.base.farmi_bill

class OrderModel(
    val name: String,
    val quantity: Long,
    val originPrice: String,
    val toMoneyPrice: String,
    val discountPrice: String? = null
)