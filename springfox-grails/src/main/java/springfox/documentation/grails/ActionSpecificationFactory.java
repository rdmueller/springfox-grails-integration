package springfox.documentation.grails;

import com.fasterxml.classmate.ResolvedType;
import grails.core.GrailsDomainClass;
import springfox.documentation.service.ResolvedMethodParameter;

import static java.util.Collections.*;
import static springfox.documentation.grails.SynthesizedAnnotations.*;


@FunctionalInterface
public interface ActionSpecificationFactory {
  default Class<?> idType(GrailsDomainClass domain) {
    return domain != null ? domain.getIdentifier().getType() : Void.TYPE;
  }

  default ResolvedMethodParameter pathParameter(int index, String name, ResolvedType resolvedType) {
    return new ResolvedMethodParameter(
        index,
        name,
        singletonList(pathVariable(name, name)),
        resolvedType);
  }

  default ResolvedMethodParameter queryParameter(
      int index,
      String name,
      ResolvedType resolvedType,
      boolean required,
      String defaultValue) {
    return new ResolvedMethodParameter(
        index,
        name,
        singletonList(requestParam(name, name, required, defaultValue)),
        resolvedType);
  }

  default ResolvedMethodParameter bodyParameter(int parameterIndex, ResolvedType resolvedType) {
    return new ResolvedMethodParameter(
        parameterIndex,
        "body",
        singletonList(SynthesizedAnnotations.REQUEST_BODY_ANNOTATION), resolvedType);
  }


  default Class domainClass(GrailsDomainClass domain) {
    if (domain != null) {
      return domain.getClazz();
    }
    return Void.TYPE;
  }

  ActionSpecification create(GrailsActionContext actionContext);
}
