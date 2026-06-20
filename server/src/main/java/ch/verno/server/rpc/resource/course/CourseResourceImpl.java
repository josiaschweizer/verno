package ch.verno.server.rpc.resource.course;

import ch.verno.contract.endpoint.course.CourseResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.server.bean.ServerBean;
import jakarta.annotation.Nonnull;

@RpcResource(CourseResource.class)
public class CourseResourceImpl implements CourseResource {

  public CourseResourceImpl(@Nonnull final ServerBean serverBean) {

  }

}
