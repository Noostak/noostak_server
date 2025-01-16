package org.noostak.server.group.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GroupNameTest {

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCases {

        @ParameterizedTest
        @DisplayName("유효한 그룹 이름으로 객체 생성")
        @CsvSource({
                "TeamAlpha",
                "한글그룹",
                "Group123",
                "ValidGroupName",
                "1234567890",
                "Mixed123한글",
                "특수문자#!!@!#",
                "띄어쓰기     "
        })
        void shouldCreateGroupNameSuccessfully(String validName) {
            GroupName groupName = GroupName.from(validName);
            assertThat(groupName.value()).isEqualTo(validName);
        }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailureCases {

        @ParameterizedTest
        @DisplayName("그룹 이름이 null이거나 비어 있는 경우")
        @NullAndEmptySource
        void shouldThrowExceptionForNullOrEmptyName(String invalidName) {
            assertThatThrownBy(() -> GroupName.from(invalidName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR] 그룹 이름은 비어 있을 수 없습니다.");
        }

        @ParameterizedTest
        @DisplayName("그룹 이름의 길이가 30자를 초과하는 경우")
        @CsvSource({
                "abcdefghijklmnopqrstuvwxyz12345",
                "1234567890123456789012345678901"
        })
        void shouldThrowExceptionForNameExceedingMaxLength(String invalidName) {
            assertThatThrownBy(() -> GroupName.from(invalidName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR] 그룹 이름의 길이는 30글자를 넘을 수 없습니다.");
        }
    }
}
