package org.noostak.server.member.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberNameTest {

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @ParameterizedTest
        @DisplayName("유효한 이름으로 객체 생성")
        @CsvSource({
                "Alice",
                "한글이름",
                "JohnDoe",
                "영한혼합"
        })
        void shouldCreateMemberNameSuccessfully(String validName) {
            MemberName memberName = MemberName.from(validName);
            assertThat(memberName.value()).isEqualTo(validName);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @ParameterizedTest
        @DisplayName("이름이 null이거나 비어 있는 경우")
        @NullAndEmptySource
        void shouldThrowExceptionForNullOrEmptyName(String invalidName) {
            assertThatThrownBy(() -> MemberName.from(invalidName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR] 멤버 이름은 비어 있을 수 없습니다.");
        }

        @ParameterizedTest
        @DisplayName("이름의 길이가 15자를 초과하는 경우")
        @CsvSource({
                "abcdefghijklmnop",
                "권장순권민성한승우이원진백창연오혜성"
        })
        void shouldThrowExceptionForNameExceedingMaxLength(String invalidName) {
            assertThatThrownBy(() -> MemberName.from(invalidName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR] 멤버 이름의 길이는 15글자를 넘을 수 없습니다.");
        }

        @ParameterizedTest
        @DisplayName("특수 문자가 포함된 이름")
        @CsvSource({
                "John_Doe",
                "Jane@Doe",
                "Alice!",
                "1234"
        })
        void shouldThrowExceptionForNameWithSpecialCharacters(String invalidName) {
            assertThatThrownBy(() -> MemberName.from(invalidName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR] 멤버 이름은 알파벳 혹은 한글로만 구성이 가능합니다.");
        }
    }
}
