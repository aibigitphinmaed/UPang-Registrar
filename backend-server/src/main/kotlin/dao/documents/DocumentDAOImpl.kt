package com.ite393group5.dao.documents

import com.ite393group5.db.DocumentRecordsTable
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction

class DocumentDAOImpl : DocumentDAO {
    override fun createDocument(
        newDocument: DocumentRecords,
        studentId: Int
    ): DocumentRecords? {
        return transaction{

            val docId = DocumentRecordsTable.insertAndGetId {
                it[this.studentId] = studentId
                it[this.documentType] = newDocument.documentType
                it[this.requestedDate] = newDocument.requestedDate
            }

            return@transaction DocumentRecords(
                    id = docId.value,
                    studentId = studentId,
                    documentType = newDocument.documentType,
                    requestedDate = newDocument.requestedDate
            )

        }
    }

    override fun updateDocument(
        documentRecords: DocumentRecords,
        studentId: Int
    ): DocumentRecords? {
        TODO("Not yet implemented")
    }

    override fun deleteDocument(
        documentRecords: DocumentRecords,
        studentId: Int
    ): Int {
        TODO("Not yet implemented")
    }

    override fun findAllDocumentByStudentId(studentId: Int): List<DocumentRecords>? {
        TODO("Not yet implemented")
    }

    override fun findDocumentById(id: Int): DocumentRecords? {
        TODO("Not yet implemented")
    }

}