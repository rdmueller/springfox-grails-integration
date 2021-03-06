package springfox.documentation.grails;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import static springfox.documentation.grails.Actions.*;

class CreateActionSpecificationFactory implements ActionSpecificationFactory {
  private final TypeResolver resolver;

  CreateActionSpecificationFactory(TypeResolver resolver) {
    this.resolver = resolver;
  }

  @Override
  public ActionSpecification create(GrailsActionContext context) {
    Map<String, HandlerMethod> actions = actionsToHandler(context.getController().getClazz());
    HandlerMethod handlerMethod = actions.get(context.getAction());
    return new ActionSpecification(
        new HashSet<>(Arrays.asList(RequestMethod.POST, RequestMethod.PUT)),
        new HashSet<>(Collections.singletonList(MediaType.APPLICATION_JSON)),
        new HashSet<>(Collections.singletonList(MediaType.APPLICATION_JSON)),
        handlerMethod,
        new ArrayList<>(Collections.singletonList(
            bodyParameter(1, resolver.resolve(domainClass(context.getDomainClass()))))),
        resolver.resolve(domainClass(context.getDomainClass())));
  }
}
