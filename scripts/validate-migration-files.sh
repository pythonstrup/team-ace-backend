#!/bin/bash

# 🔍 Flyway Migration 검증 도구
# 마이그레이션 파일을 GitHub에 올리기 전에 로컬에서 검증합니다

echo "🚀 Flyway 마이그레이션 검증을 시작합니다..."
echo ""

MIGRATION_DIR="src/main/resources/db/migration"

# 마이그레이션 폴더가 있는지 확인
if [ ! -d "$MIGRATION_DIR" ]; then
    echo "❌ 마이그레이션 폴더를 찾을 수 없습니다: $MIGRATION_DIR"
    exit 1
fi

echo "1️⃣ 파일 이름 확인 중..."
echo "   (V1__description.sql 형식이어야 합니다)"

# 잘못된 파일 이름 찾기
wrong_files=$(find "$MIGRATION_DIR" -name "*.sql" | grep -v -E "V[0-9]+__.*\.sql$" || true)

if [ ! -z "$wrong_files" ]; then
    echo "❌ 잘못된 파일 이름을 발견했습니다:"
    echo "$wrong_files" | while read file; do
        echo "   → $(basename "$file")"
    done
    echo ""
    echo "💡 올바른 예시: V1__Create_user_schema.sql"
    exit 1
fi

echo "✅ 모든 파일 이름이 올바릅니다"
echo ""

echo "2️⃣ 중복 버전 번호 확인 중..."

# 버전 번호 중복 확인
versions=$(find "$MIGRATION_DIR" -name "*.sql" | sed -E 's/.*\/(V[0-9]+)__.*/\1/' | sort)
duplicates=$(echo "$versions" | uniq -d)

if [ ! -z "$duplicates" ]; then
    echo "❌ 중복된 버전 번호를 발견했습니다:"
    echo "$duplicates" | while read dup; do
        echo "   → $dup"
    done
    exit 1
fi

echo "✅ 중복된 버전 번호가 없습니다"
echo ""

echo "3️⃣ 현재 마이그레이션 파일 목록:"
echo ""

find "$MIGRATION_DIR" -name "*.sql" | sort | while read file; do
    filename=$(basename "$file")
    version=$(echo "$filename" | sed -E 's/(V[0-9]+)__.*/\1/')
    description=$(echo "$filename" | sed -E 's/V[0-9]+__(.*)\.sql/\1/' | tr '_' ' ')
    
    echo "   $version: $description"
done

echo ""
echo "🎉 파일 검증이 완료되었습니다!"
echo "   SQL 문법 검증은 './gradlew flywayValidate' 명령으로 확인해주세요."