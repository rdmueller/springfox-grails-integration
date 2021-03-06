package springfox.documentation.grails

import com.fasterxml.classmate.TypeResolver
import grails.core.GrailsControllerClass
import grails.core.GrailsDomainClass
import grails.core.GrailsDomainClassProperty
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMethod
import spock.lang.Specification

class EditActionSpecificationFactorySpec extends Specification {
  def controller = Mock(GrailsControllerClass)
  def domain = Mock(GrailsDomainClass)
  def identifierProperty = Mock(GrailsDomainClassProperty)

  def setup() {
    controller.clazz >> AController
    domain.clazz >> ADomain
    domain.identifier >> identifierProperty
    domain.identifier.type >> Integer
  }

  def "Edit action produces action specification" () {
    given:
      def resolver = new TypeResolver()
      def sut = new EditActionSpecificationFactory(resolver)
    when:
      def spec = sut.create(new GrailsActionContext(controller, domain, "edit"))
    then:
      spec.consumes == [MediaType.APPLICATION_JSON] as Set
      spec.produces == [MediaType.APPLICATION_JSON] as Set
      spec.supportedMethods == [RequestMethod.PUT, RequestMethod.POST] as Set
      spec.parameters.size() == 2
      spec.parameters[0].parameterType == resolver.resolve(Integer)
      spec.parameters[0].parameterIndex == 1
      spec.parameters[0].defaultName().isPresent()
      spec.parameters[0].defaultName().get() == "id"

      spec.parameters[1].parameterType == resolver.resolve(ADomain)
      spec.parameters[1].parameterIndex == 2
      spec.parameters[1].defaultName().isPresent()
      spec.parameters[1].defaultName().get() == "body"

      spec.returnType == resolver.resolve(ADomain)
      spec.handlerMethod.method == AController.methods.find {it.name == "edit" }
  }

  def "Edit action throws exception when action is not found" () {
    given:
      def resolver = new TypeResolver()
      def sut = new EditActionSpecificationFactory(resolver)
    when:
      sut.create(new GrailsActionContext(controller, domain, "unknown"))
    then:
      def exception = thrown(NullPointerException)
      exception.message.contains("Handler method is null")
  }
}
