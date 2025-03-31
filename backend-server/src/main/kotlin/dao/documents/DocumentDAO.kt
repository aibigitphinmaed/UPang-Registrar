package com.ite393group5.dao.documents

interface DocumentDAO {
    fun createDocument(newDocument:DocumentRecords, studentId:Int): DocumentRecords?
    fun updateDocument(documentRecords:DocumentRecords, studentId:Int): DocumentRecords?
    fun deleteDocument(documentRecords:DocumentRecords, studentId:Int): Int
    fun findAllDocumentByStudentId(studentId:Int): List<DocumentRecords>?
    fun findDocumentById(id:Int): DocumentRecords?
}