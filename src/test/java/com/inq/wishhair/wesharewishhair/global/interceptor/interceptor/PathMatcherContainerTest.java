package com.inq.wishhair.wesharewishhair.global.interceptor.interceptor;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpMethod.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("[PathMatcherContainer 테스트] - Global")
class PathMatcherContainerTest {

	private final PathMatcherContainer pathMatcherContainer;

	public PathMatcherContainerTest() {
		//given
		this.pathMatcherContainer = new PathMatcherContainer();
		pathMatcherContainer.includePathPattern("/include/**", GET);
		pathMatcherContainer.includePathPattern("/path/include", GET);

		pathMatcherContainer.excludePathPattern("/exclude/**", GET);
		pathMatcherContainer.excludePathPattern("/path/exclude", GET);

		pathMatcherContainer.includePathPattern("/exclude/path", GET);
	}

	@Nested
	@DisplayName("[인터셉터가 적용되어아햐는 요청인지 확인한다]")
	class isInterceptorRequired {

		@Test
		@DisplayName("[적용되어아야하는 요청으로 true 를 반환한다]")
		void returnTrue() {
			//when
			boolean actual1 = pathMatcherContainer.isInterceptorRequired("/include/path", GET);
			boolean actual2 = pathMatcherContainer.isInterceptorRequired("/path/include", GET);

			//then
			assertAll(
				() -> assertThat(actual1).isTrue(),
				() -> assertThat(actual2).isTrue()
			);
		}

		@Nested
		@DisplayName("[적용되지않아야하는 요청으로 false 를 반환한다]")
		class returnFalse {

			@Test
			@DisplayName("[include 패턴에 포함되지만 exclude 패턴에도 포함되어서 false 를 반환한다]")
			void returnFalse1() {
				//when
				boolean actual = pathMatcherContainer.isInterceptorRequired("/exclude/path", GET);

				//then
				assertThat(actual).isFalse();
			}

			@Test
			@DisplayName("[path 가 include 패턴에 포함되지 않아 false 를 반환한다]")
			void returnFalse2() {
				//when
				boolean actual = pathMatcherContainer.isInterceptorRequired("/hello/path", GET);

				//then
				assertThat(actual).isFalse();
			}

			@Test
			@DisplayName("[메소드가 맞지 않아 false 를 반환한다]")
			void returnFalse3() {
				//when
				boolean actual = pathMatcherContainer.isInterceptorRequired("/include/path", POST);

				//then
				assertThat(actual).isFalse();
			}
		}
	}
}