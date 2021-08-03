package com.soda1127.example.bookstore.data.response

import com.soda1127.example.bookstore.data.entity.BookInfoEntity
import com.soda1127.example.bookstore.data.entity.PDFEntity

/**
{
"error":"0",
"title":"TypeScript Notes for Professionals",
"subtitle":"",
"authors":"Stack Overflow Community",
"publisher":"Self-publishing",
"language":"English",
"isbn10":"1622115724",
"isbn13":"1001622115721",
"pages":"96",
"year":"2018",
"rating":"0",
"desc":"The TypeScript Notes for Professionals book is compiled from Stack Overflow Documentation, the content is written by the beautiful people at Stack Overflow....",
"price":"$0.00",
"image":"https://itbook.store/img/books/1001622115721.png",
"url":"https://itbook.store/books/1001622115721",
"pdf":{
"Free eBook":"https://www.dbooks.org/d/5592544360-1622115253-9bbc1cd0a894d0c9/"
}
}
 */

data class BookInfoResponse(
    val error: String,
    val title: String,
    val subtitle: String,
    val authors: String,
    val publisher: String,
    val language: String,
    val isbn10: String,
    val isbn13: String,
    val pages: String,
    val year: String,
    val rating: String,
    val desc: String,
    val price: String,
    val image: String,
    val url: String,
    val pdf: PDFEntity?
) {

    fun toEntity() = BookInfoEntity(
        title,
        subtitle,
        authors,
        publisher,
        language,
        isbn10,
        isbn13,
        pages.toInt(),
        year.toInt(),
        rating.toDouble().toFloat(),
        desc,
        price,
        image,
        url,
        pdf
    )

}
