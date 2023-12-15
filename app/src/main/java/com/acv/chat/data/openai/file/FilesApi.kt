package com.acv.chat.data.openai.file

import arrow.core.raise.Raise
import com.acv.chat.arrow.error.catch
import com.acv.chat.data.openai.assistant.file.FileId
import com.acv.chat.data.openai.chat.appendFileSource
import com.acv.chat.domain.DomainError
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get


context(com.acv.chat.data.openai.common.OpenAIClient)
class FilesApi {

  context(Raise<DomainError>)
  suspend fun upload(
    request: FileUpload
  ): File =
    catch(
      onError = DomainError::UnknownDomainError
    ) {
      val response = client.submitFormWithBinaryData(
        url = "files",
        formData = formData {
          appendFileSource("file", request.file)
          append(key = "purpose", value = request.purpose.raw)
        }
      )

      response.body()
    }

  context(Raise<DomainError>)
  suspend fun files(): ListResponseFile =
    catch(
      onError = DomainError::UnknownDomainError
    ) {
      val response = client.get("files")

      response.body()
    }

  context(Raise<DomainError>)
  suspend fun file(fileId: FileId): File =
    catch(
      onError = DomainError::UnknownDomainError
    ) {
      val response = client.get("files/${fileId.id}")
      response.body()
    }

  context(Raise<DomainError>)
  suspend fun delete(fileId: FileId): DeletionStatus =
    catch(
      onError = DomainError::UnknownDomainError
    ) {
      val response = client.delete("files/${fileId.id}")
      response.body()
    }

  context(Raise<DomainError>)
  suspend fun download(fileId: FileId): ByteArray =
    catch(
      onError = DomainError::UnknownDomainError
    ) {
      val response = client.get("files/${fileId.id}/content")
      response.body()
    }

}
