package com.ymmihw.mongodb.simple.tagging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link TagRepository}.
 * 
 * @author Donato Rimenti
 *
 */
public class TaggingIntegrationTest {

  /**
   * Object to test. Object to test.
   */
  private static TagRepository repository = new TagRepository();

  /**
   * Sets the test up by instantiating the object to test.
   */

  @BeforeAll
  public static void beforeClass() {
    repository.addPost(Post.builder().title("Post 1").tags(Arrays.asList("MongoDB")).build());
    repository.addPost(Post.builder().title("Post 2").tags(Arrays.asList("MongoDB")).build());
    repository
        .addPost(Post.builder().title("Post 3").tags(Arrays.asList("MongoDB", "Java 8")).build());
    repository.addPost(Post.builder().title("Post 4").tags(Arrays.asList("Java 8")).build());
  }

  /**
   * Tests {@link TagRepository#postsWithAtLeastOneTag(String...)} with 1 tag.
   */
  @Test
  public void givenTagRepository_whenPostsWithAtLeastOneTagMongoDB_then3Results() {
    List<Post> results = repository.postsWithAtLeastOneTag("MongoDB");
    results.forEach(System.out::println);

    assertEquals(3, results.size());
    results.forEach(post -> {
      assertTrue(post.getTags().contains("MongoDB"));
    });

  }

  /**
   * Tests {@link TagRepository#postsWithAtLeastOneTag(String...)} with 2 tags.
   */
  @Test
  public void givenTagRepository_whenPostsWithAtLeastOneTagMongoDBJava8_then4Results() {
    List<Post> results = repository.postsWithAtLeastOneTag("MongoDB", "Java 8");
    results.forEach(System.out::println);

    assertEquals(4, results.size());
    results.forEach(post -> {
      assertTrue(post.getTags().contains("MongoDB") || post.getTags().contains("Java 8"));
    });
  }

  /**
   * Tests {@link TagRepository#postsWithAllTags(String...)} with 1 tag.
   */
  @Test
  public void givenTagRepository_whenPostsWithAllTagsMongoDB_then3Results() {
    List<Post> results = repository.postsWithAllTags("MongoDB");
    results.forEach(System.out::println);

    assertEquals(3, results.size());
    results.forEach(post -> {
      assertTrue(post.getTags().contains("MongoDB"));
    });
  }

  /**
   * Tests {@link TagRepository#postsWithAllTags(String...)} with 2 tags.
   */
  @Test
  public void givenTagRepository_whenPostsWithAllTagsMongoDBJava8_then2Results() {
    List<Post> results = repository.postsWithAllTags("MongoDB", "Java 8");
    results.forEach(System.out::println);

    assertEquals(1, results.size());
    results.forEach(post -> {
      assertTrue(post.getTags().contains("MongoDB"));
      assertTrue(post.getTags().contains("Java 8"));
    });
  }

  /**
   * Tests {@link TagRepository#postsWithoutTags(String...)} with 1 tag.
   */
  @Test
  public void givenTagRepository_whenPostsWithoutTagsMongoDB_then1Result() {
    List<Post> results = repository.postsWithoutTags("MongoDB");
    results.forEach(System.out::println);

    assertEquals(1, results.size());
    results.forEach(post -> {
      assertFalse(post.getTags().contains("MongoDB"));
    });
  }

  /**
   * Tests {@link TagRepository#postsWithoutTags(String...)} with 2 tags.
   */
  @Test
  public void givenTagRepository_whenPostsWithoutTagsMongoDBJava8_then0Results() {
    List<Post> results = repository.postsWithoutTags("MongoDB", "Java 8");
    results.forEach(System.out::println);

    assertEquals(0, results.size());
    results.forEach(post -> {
      assertFalse(post.getTags().contains("MongoDB"));
      assertFalse(post.getTags().contains("Java 8"));
    });
  }

  /**
   * Tests {@link TagRepository#addTags(String, List)} and
   * {@link TagRepository#removeTags(String, List)}. These tests run together to keep the database
   * in a consistent state.
   */
  @Test
  public void givenTagRepository_whenAddingRemovingElements_thenNoDuplicates() {
    // Adds one element and checks the result.
    boolean result = repository.addTags("Post 1", Arrays.asList("jUnit", "jUnit5"));
    assertTrue(result);

    // We add the same elements again to check that there's no duplication.
    result = repository.addTags("Post 1", Arrays.asList("jUnit", "jUnit5"));
    assertFalse(result);

    // Fetches the element back to check if the elements have been added.
    List<Post> postsAfterAddition = repository.postsWithAllTags("jUnit", "jUnit5");
    assertEquals(1, postsAfterAddition.size());
    postsAfterAddition.forEach(post -> {
      assertTrue(post.getTags().contains("jUnit"));
      assertTrue(post.getTags().contains("jUnit5"));
    });

    // Checks for duplication.
    long countDuplicateTags =
        StreamSupport.stream(postsAfterAddition.get(0).getTags().spliterator(), false)
            .filter(x -> x.equals("jUnit5")).count();
    assertEquals(1, countDuplicateTags);

    // Tries to remove the tags added.
    result = repository.removeTags("Post 1", Arrays.asList("jUnit", "jUnit5"));
    assertTrue(result);

    // We remove the same elements again to check for errors.
    result = repository.removeTags("Post 1", Arrays.asList("jUnit", "jUnit5"));
    assertFalse(result);

    // Fetches the element back to check if the elements have been removed.
    List<Post> postsAfterDeletion = repository.postsWithAllTags("jUnit", "jUnit5");
    assertEquals(0, postsAfterDeletion.size());
    postsAfterDeletion = repository.postsWithAtLeastOneTag("jUnit");
    assertEquals(0, postsAfterDeletion.size());
    postsAfterDeletion = repository.postsWithAtLeastOneTag("jUnit5");
    assertEquals(0, postsAfterDeletion.size());
  }

}
