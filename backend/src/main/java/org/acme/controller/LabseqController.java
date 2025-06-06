package org.acme.controller;

import java.math.BigInteger;

import org.acme.service.LabseqService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/labseq")
@Tag(name = "Labseq", description = "Provides values from the Labseq sequence")
public class LabseqController {

	@Inject
	private LabseqService service;

	@GET
	@Path("/{index}")
	@Produces(MediaType.TEXT_PLAIN)
	@Operation(summary = "Calculate Labseq value", description = "Returns the Labseq sequence value at the given index.")
	@APIResponses({
			@APIResponse(responseCode = "200", description = "Labseq value returned successfully"),
			@APIResponse(responseCode = "400", description = "Invalid input: index must be a non-negative integer"),
			@APIResponse(responseCode = "500", description = "Unexpected internal server error")
	})
	public Response calculateLabseq(@PathParam("index") int index) {
		try {
			BigInteger result = service.calculate(index);
			return Response.ok(result.toString()).build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Index must be a non-negative integer.")
					.type(MediaType.TEXT_PLAIN)
					.build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("An unexpected error occurred.")
					.type(MediaType.TEXT_PLAIN)
					.build();
		}
	}
}
