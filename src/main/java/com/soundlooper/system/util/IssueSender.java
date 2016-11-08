package com.soundlooper.system.util;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.soundlooper.system.preferences.SoundLooperProperties;

public class IssueSender {
	public static void sendIssue(String title, String body) {
		Client client = ClientBuilder.newBuilder().build();
		WebTarget target = client.target(SoundLooperProperties.getInstance().getRepoPathIssues());
		Builder request = target.request(MediaType.APPLICATION_JSON_TYPE);
		Response result = request.post(Entity.entity(
				"{\"title\": \"Found a bug\", \"body\": \"I'm having a problem with this.\"}",
				MediaType.APPLICATION_JSON_TYPE));
		client.close();
	}

	public static void main(String[] args) {
		sendIssue("test", "Test de création d'erreur");
	}

}
